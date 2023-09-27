package com.shall_we.login

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.user.UserApiClient
import com.shall_we.App
import com.shall_we.R
import com.shall_we.databinding.FragmentKakaoLoginBinding
import com.shall_we.login.data.Auth
import com.shall_we.login.data.AuthResponse
import com.shall_we.login.data.AuthSignService
import com.shall_we.login.data.IAuthSign
import com.shall_we.login.signup.PhoneAuthFragment

class kakaoLoginFragment : Fragment() , IAuthSign {

    private lateinit var auth : Auth
    private var loginEvent: ILoginEvent? = null

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
        val binding = FragmentKakaoLoginBinding.inflate(inflater, container, false)
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Log.e("login", "로그인 실패 $error")

            } else if (token != null) {
                Log.d("login", "카카오계정으로 로그인 성공 access ${token.accessToken}")
                getKakaoUserData()

            }
        }

        //        // 로그인 조합 예제
//// 로그인 정보 확인
//        UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
//            if (error != null) {
//                Log.e("login", "카카오계정으로 로그인 실패", error)
//
//            }
//            else if (tokenInfo != null) {
//                Log.i("login", "카카오계정으로 로그인 성공 ${tokenInfo}")
//            }
//        }
//
        binding.loginBtn.setOnClickListener {
            Log.d("test", "key hash: ${Utility.getKeyHash(requireContext())}")

//            여러 명이 하나의 프로젝트를 개발하는 경우 모든 사람들이 각자의 디버그 키 해시 값을 구해서 다 등록해야 함.
//            배포 시에는 디버그 키가 아닌 릴리즈 키 해시 값을 등록해야 함

            // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
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
        return binding.root
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
                    (user.id).toString(),
                    user.kakaoAccount?.profile?.nickname!!,
                    user.kakaoAccount?.email!!,
                    user.kakaoAccount?.profile?.thumbnailImageUrl!!)
                Log.d("login","$auth")
                AuthSignService(this).postAuthSignUp(auth)
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
        setUserData(response)
        loginEvent?.onLoginSuccess()
        fragmentChangeSignUp()

    }

    override fun onPostAuthSignUpFailed(message: String) {
        loginEvent?.onLoginFailed(message)
        if(message == "로그인"){
            AuthSignService(this).postAuthSignIn(auth)
        }
    }

    private fun setUserData(response: AuthResponse){
        val sharedPref = context?.getSharedPreferences("MY_APP_PREFS", Context.MODE_PRIVATE)
        val accessToken = response.data.accessToken
        sharedPref?.edit()?.putString("ACCESS_TOKEN", accessToken)?.apply()
        val refreshToken = response.data.refreshToken
        sharedPref?.edit()?.putString("REFRESH_TOKEN", refreshToken)?.apply()

        App.accessToken = sharedPref?.getString("ACCESS_TOKEN", null)
        App.refreshToken = sharedPref?.getString("REFRESH_TOKEN", null)

        Log.d("login","access token ${App.accessToken}")
        Log.d("login", "refresh token ${App.refreshToken}")
    }

    private fun fragmentChangeLogin(){
        val newFragment = PhoneAuthFragment() // 전환할 다른 프래그먼트 객체 생성
        val bundle = Bundle()
        newFragment.arguments = bundle
        // 프래그먼트 전환
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView3, newFragment)
            .addToBackStack(null)
            .commit()
    }

    private fun fragmentChangeSignUp(){
        val newFragment = PhoneAuthFragment() // 전환할 다른 프래그먼트 객체 생성
        val bundle = Bundle()
        newFragment.arguments = bundle
        // 프래그먼트 전환
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView3, newFragment)
            .addToBackStack(null)
            .commit()
    }
}