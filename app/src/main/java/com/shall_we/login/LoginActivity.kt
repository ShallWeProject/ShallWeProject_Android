package com.shall_we.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.user.UserApiClient
import com.shall_we.R
import com.shall_we.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        binding = ActivityLoginBinding.inflate(layoutInflater)

//        // 로그인 조합 예제
//// 로그인 정보 확인
//        UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
//            if (error != null) {
//                Log.e("login", "카카오계정으로 로그인 실패", error)
//
//            }
//            else if (tokenInfo != null) {
//                Log.i("login", "카카오계정으로 로그인 성공 ${tokenInfo}")
//
//            }
//        }
//
//        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
//            if (error != null) {
//                when {
//                    error.toString() == AuthErrorCause.AccessDenied.toString() -> {
//                        Log.d("login","접근이 거부 됨(동의 취소)")
//                    }
//                    error.toString() == AuthErrorCause.InvalidClient.toString() -> {
//                        Log.d("login","유효하지 않은 앱")
//
//                    }
//                    error.toString() == AuthErrorCause.InvalidGrant.toString() -> {
//                        Log.d("login","인증 수단이 유효하지 않아 인증할 수 없는 상태")
//
//                    }
//                    error.toString() == AuthErrorCause.InvalidRequest.toString() -> {
//                        Log.d("login","요청 파라미터 오류")
//
//                    }
//                    error.toString() == AuthErrorCause.InvalidScope.toString() -> {
//                        Log.d("login","유효하지 않은 scope ID")
//
//                    }
//                    error.toString() == AuthErrorCause.Misconfigured.toString() -> {
//                        Log.d("login","설정이 올바르지 않음(android key hash)")
//
//                    }
//                    error.toString() == AuthErrorCause.ServerError.toString() -> {
//                        Log.d("login","서버 내부 에러")
//
//                    }
//                    error.toString() == AuthErrorCause.Unauthorized.toString() -> {
//                        Log.d("login","앱이 요청 권한이 없음")
//
//                    }
//                    else -> { // Unknown
//                        Log.d("login","기타 에러")
//
//                    }
//                }
//            }
//            else if (token != null) {
//                Log.d("login","로그인에 성공하였습니다.")
//
//            }
//        }

        // 카카오계정으로 로그인 공통 callback 구성
//        // 카카오톡으로 로그인 할 수 없어 카카오계정으로 로그인할 경우 사용됨
//        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
//            if (error != null) {
//                Log.e("login", "카카오계정으로 로그인 실패", error)
//            } else if (token != null) {
//                Log.i("login", "카카오계정으로 로그인 성공 ${token.accessToken}")
//            }
//        }

        binding.loginBtn.setOnClickListener{
            Log.d("test", "key hash: ${Utility.getKeyHash(this)}")

//            여러 명이 하나의 프로젝트를 개발하는 경우 모든 사람들이 각자의 디버그 키 해시 값을 구해서 다 등록해야 함.
//            배포 시에는 디버그 키가 아닌 릴리즈 키 해시 값을 등록해야 함
//            KakaoSdk.init(this, "00e76c39754ce94d507a356e3f330371")  // native app key
//            Log.d("test", "after Kakao Sdk init")

            // 카카오계정으로 로그인 공통 callback 구성
            // 카카오톡으로 로그인 할 수 없어 카카오계정으로 로그인할 경우 사용됨
            val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
                Log.d("test", "callback")
                if (error != null) {
                    Log.d("test", "카카오계정으로 로그인 실패", error)
                }
                else if (token != null) {
                    Log.d("test", "카카오계정으로 로그인 성공 ${token.accessToken}")
                    startActivity(intent)
                    overridePendingTransition(0,0)

                }
            }

            // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
            if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
                UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                    if (error != null) {
                        Log.d("test", "카카오톡으로 로그인 실패", error)
                        // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                        // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                        if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                            return@loginWithKakaoTalk
                        }

                        // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                        UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
                    } else if (token != null) {
                        Log.d("test", "카카오톡으로 로그인 성공 ${token.accessToken}")
                    }
                }

            } else {
                UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
            }
        }
//        // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
//        if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
//            UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
//                if (error != null) {
//                    Log.e("login", "카카오톡으로 로그인 실패", error)
//
//                    // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
//                    // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
//                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
//                        return@loginWithKakaoTalk
//                    }
//
//                    // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
//                    UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
//                } else if (token != null) {
//                    Log.i("login", "카카오톡으로 로그인 성공 ${token.accessToken}")
//                }
//            }
//        } else {
//            UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
//        }
    }
}