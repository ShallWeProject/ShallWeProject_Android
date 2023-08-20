package com.shall_we.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.AuthErrorCause
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.user.UserApiClient
import com.shall_we.App
import com.shall_we.R
import com.shall_we.databinding.ActivityLoginBinding
import com.shall_we.login.data.Auth
import com.shall_we.login.data.AuthResponse
import com.shall_we.login.data.AuthSignService
import com.shall_we.login.data.IAuthSign
import com.shall_we.retrofit.RetrofitManager
import com.shall_we.search.SearchFragment

class LoginActivity : AppCompatActivity(),ILoginEvent{
    lateinit var binding: ActivityLoginBinding
    private lateinit var kakaoLoginFragment: kakaoLoginFragment
    private lateinit var phoneAuthFragment: PhoneAuthFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // kakaoLoginFragment 인스턴스화
        kakaoLoginFragment = kakaoLoginFragment()
        kakaoLoginFragment.setLoginEventListener(this)
        phoneAuthFragment = PhoneAuthFragment()

        // 프래그먼트를 처음에 추가합니다.
        supportFragmentManager.beginTransaction()
            .add(R.id.fragmentContainerView3, phoneAuthFragment)
            .commit()

    }

    override fun onLoginSuccess() {
        val newFragment = kakaoLoginFragment() // 전환할 다른 프래그먼트 객체 생성
        val bundle = Bundle()
        newFragment.arguments = bundle

        // 프래그먼트 전환
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView3, newFragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onLoginFailed(message: String) {
    }
}