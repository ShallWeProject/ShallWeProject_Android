package com.shall_we

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.kakao.sdk.auth.Constants.ACCESS_TOKEN
import com.kakao.sdk.auth.Constants.REFRESH_TOKEN
import com.kakao.sdk.common.KakaoSdk
import com.shall_we.login.data.Auth
import retrofit2.Retrofit

class App : Application() {

    companion object{
        lateinit var instance: App
        lateinit var context: Context

        lateinit var sharedPreferences: SharedPreferences

        var accessToken:String? = ACCESS_TOKEN
        var refreshToken:String? = REFRESH_TOKEN
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        context = applicationContext

        KakaoSdk.init(this, getString(R.string.kakao_app_key))
        sharedPreferences =
            applicationContext.getSharedPreferences("com.shall_we", MODE_PRIVATE)
    }

}