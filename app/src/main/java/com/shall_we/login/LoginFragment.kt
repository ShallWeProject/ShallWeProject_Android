package com.shall_we.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import com.google.android.gms.tasks.Task
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.user.UserApiClient
import com.shall_we.App
import com.shall_we.BuildConfig
import com.shall_we.R
import com.shall_we.databinding.FragmentLoginBinding
import com.shall_we.login.data.Auth
import com.shall_we.login.data.AuthResponse
import com.shall_we.login.data.AuthSignService
import com.shall_we.login.data.AuthTokenData
import com.shall_we.login.data.IAuthSign
import com.shall_we.login.data.LoginGoogleResult
import com.shall_we.login.signin.LoginSuccessFragment
import com.shall_we.login.signup.PhoneAuthFragment
import com.shall_we.retrofit.RESPONSE_STATE
import com.shall_we.retrofit.RetrofitManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.launch

class LoginFragment : Fragment() , IAuthSign {


    private lateinit var auth : Auth
    private var loginEvent: ILoginEvent? = null

    private val RC_SIGN_IN = 9001

    private val googleSignInClient: GoogleSignInClient by lazy { getGoogleClient() }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onResume() {
        super.onResume()
        val supportActionBar = (requireActivity() as AppCompatActivity).supportActionBar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentLoginBinding.inflate(inflater, container, false)
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Log.e("login", "로그인 실패 $error")

            } else if (token != null) {
                Log.d("login", "카카오계정으로 로그인 성공 access ${token.accessToken}")
                getKakaoUserData()

            }
        }

        binding.btnKakaoLogin.setOnClickListener {
            Log.d("test", "key hash: ${Utility.getKeyHash(requireContext())}")

            if (UserApiClient.instance.isKakaoTalkLoginAvailable(requireContext())) {
                UserApiClient.instance.loginWithKakaoTalk(requireContext()) { token, error ->
                    if (error != null) {
                        Log.d("login", "카카오톡으로 로그인 실패", error)
                        // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                        // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                        if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                            return@loginWithKakaoTalk
                        }

                        // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                        UserApiClient.instance.loginWithKakaoAccount(requireContext(), callback = callback)
                    } else if (token != null) {
                        Log.d("login", "카카오톡으로 로그인 성공 ${token.accessToken}")
                        getKakaoUserData()
                    }
                }

            } else {
                UserApiClient.instance.loginWithKakaoAccount(requireContext(), callback = callback)
            }
        }
        binding.btnGoogleLogin.setOnClickListener {
            onGoogleLoginClick()
        }


        return binding.root
    }

    private fun getGoogleClient(): GoogleSignInClient {

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestServerAuthCode(BuildConfig.GOOGLE_CLIENT_ID)
            .build()


        return GoogleSignIn.getClient(requireActivity(), gso)
    }

    fun onGoogleLoginClick() {
        googleSignInClient.signOut()
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            CoroutineScope(Dispatchers.Main).launch {
                handleSignInResult(task)
            }
        }
    }

    private suspend fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            val providerId = account?.id
            val email = account?.email
            val token = account?.serverAuthCode
            val provider = "GOOGLE"

            lifecycleScope.launch {
                try {
                    val result = AuthSignService(this@LoginFragment).fetchGoogleAuthInfo(account.serverAuthCode.toString())

                    // 결과를 처리
                    when (result) {
                        is LoginGoogleResult.Success -> {
                            val loginGoogleRes = result.data
                            auth = Auth(
                                providerId = providerId.toString(),
                                provider = provider,
                                email = email,
                                refreshToken = loginGoogleRes.refresh_token)

                            AuthSignService(this@LoginFragment).postAuthSignIn(auth)
                        }
                        is LoginGoogleResult.Error -> {
                            Log.d("handleSignInResult","error")
                        }

                        else -> {}
                    }
                } catch (e: Exception) {
                    // 예외 처리
                    e.printStackTrace()
                }
            }

        } catch (e: ApiException) {
            Log.w("login", "signInResult:failed code=" + e.statusCode)
        }
    }


    fun setLoginEventListener(listener: ILoginEvent) {
        loginEvent = listener
    }


    private fun getKakaoUserData(){
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e("login", "사용자 정보 요청 실패", error)
            }
            else if (user != null) {
                Log.i("login", "사용자 정보 요청 성공" +
                        "\n회원번호: ${user.id}" +
                        "\n이메일: ${user.kakaoAccount?.email}" +
                        "\n닉네임: ${user.kakaoAccount?.profile?.nickname}" +
                        "\n프로필사진: ${user.kakaoAccount?.profile?.thumbnailImageUrl}")
                auth = Auth(
                    providerId = (user.id).toString(),
                    provider = "KAKAO",
                    email = user.kakaoAccount?.email.toString(),
                    refreshToken = null)

                Log.d("login","$auth")
                AuthSignService(this).postAuthSignIn(auth)
            }
        }
    }

    override fun onPostAuthSignInSuccess(response: AuthResponse) {
        Log.d("login","onPostAuthSignInSuccess ${response.toString()}")
        setUserData(response)
        loginEvent?.onLoginSuccess()
        fragmentChangeLogin()
    }

    override fun onPostAuthSignInFailed(message: String) {
        Log.d("login","onPostAuthSignInFailed $message")
    }

    override fun onPostAuthSignUpSuccess(response: AuthResponse) {
        Log.d("login","onPostAuthSignUpSuccess ${response.toString()}")
        loginEvent?.onLoginSuccess()
        fragmentChangeSignUp(response.data)

    }

    override fun onPostAuthSignUpFailed(message: String) {
        loginEvent?.onLoginFailed(message)
        if(message == "로그인"){
            AuthSignService(this).postAuthSignIn(auth)
        }
    }

    private fun setUserData(response: AuthResponse){
        val sharedPref = context?.getSharedPreferences("com.shall_we", Context.MODE_PRIVATE)
        val accessToken = response.data.accessToken
        sharedPref?.edit()?.putString("access_token", accessToken)?.apply()
        val refreshToken = response.data.refreshToken
        sharedPref?.edit()?.putString("refresh_token", refreshToken)?.apply()

        App.accessToken = sharedPref?.getString("access_token", null)
        App.refreshToken = sharedPref?.getString("refresh_token", null)

        Log.d("login","access token ${App.accessToken}")
        Log.d("login", "refresh token ${App.refreshToken}")
    }

    private fun fragmentChangeLogin(){
        Log.d("login","fragmentChangeLogin")
        val newFragment = LoginSuccessFragment() // 전환할 다른 프래그먼트 객체 생성
        val bundle = Bundle()
        newFragment.arguments = bundle
        // 프래그먼트 전환
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView3, newFragment)
            .addToBackStack(null)
            .commit()

    }

    private fun fragmentChangeSignUp(response: AuthTokenData){
        val newFragment = PhoneAuthFragment() // 전환할 다른 프래그먼트 객체 생성
        val bundle = Bundle()
        bundle.putParcelable("token",response)
        newFragment.arguments = bundle
        // 프래그먼트 전환
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView3, newFragment)
            .addToBackStack(null)
            .commit()
    }

}