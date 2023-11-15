package com.shall_we.login

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import com.shall_we.App
import com.shall_we.MainActivity
import com.shall_we.R
import com.shall_we.databinding.ActivityLoginBinding
import com.shall_we.login.signup.PhoneAuthFragment

class LoginActivity : AppCompatActivity(),ILoginEvent{
    lateinit var binding: ActivityLoginBinding
    private lateinit var kakaoLoginFragment: LoginFragment
    private lateinit var phoneAuthFragment: PhoneAuthFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val toolbar : Toolbar = binding.tbLogin
        setSupportActionBar(toolbar)

        getSupportActionBar()?.setDisplayShowTitleEnabled(false); // 기본 타이틀 사용 안함

        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_btn) // 뒤로가기 버튼 이미지 설정
        supportActionBar?.setTitle("")


        // kakaoLoginFragment 인스턴스화
        kakaoLoginFragment = LoginFragment()
        kakaoLoginFragment.setLoginEventListener(this)
        phoneAuthFragment = PhoneAuthFragment()

        // 프래그먼트를 처음에 추가합니다.
        supportFragmentManager.beginTransaction()
            .add(R.id.fragmentContainerView3, kakaoLoginFragment)
            .commit()

        checkAutoLogin()

        val sharedPref = this.getSharedPreferences("MY_APP_PREFS", Context.MODE_PRIVATE)
        
        App.refreshToken = sharedPref.getString("REFRESH_TOKEN", null)
        App.accessToken = sharedPref.getString("ACCESS_TOKEN", null)

    }
    private fun checkAutoLogin() {
        val sharedPref = this.getSharedPreferences("MY_APP_PREFS", Context.MODE_PRIVATE)

        val accessToken = sharedPref.getString("ACCESS_TOKEN", null)
        val refreshToken = sharedPref.getString("ACCESS_TOKEN", null)
        if (accessToken != null && accessToken.isNotEmpty()) {
            Log.d("login","$accessToken")
            // accessToken이 존재할 경우 자동 로그인 처리
            loginSuccess()
        } else {
            // accessToken이 없을 경우 로그인 화면으로 이동
            showLoginFragment()
        }
    }
    fun loginSuccess(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
    fun showLoginFragment(){
        val newFragment = LoginFragment() // 전환할 다른 프래그먼트 객체 생성
        val bundle = Bundle()
        newFragment.arguments = bundle

        // 프래그먼트 전환
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView3, newFragment)
            .addToBackStack(null)
            .commit()
    }


    override fun onLoginSuccess() {
        val newFragment = LoginFragment() // 전환할 다른 프래그먼트 객체 생성
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
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                //toolbar의 back키 눌렀을 때 동작
                // 액티비티 이동
//                Log.d("toolbar","툴바 뒤로가기 버튼 클릭")
                if (supportFragmentManager.backStackEntryCount > 1) {
                    supportFragmentManager.popBackStackImmediate(null, 0)
                }else{
                    supportFragmentManager.popBackStackImmediate(null, 0)
                    supportActionBar?.setDisplayHomeAsUpEnabled(false)
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}