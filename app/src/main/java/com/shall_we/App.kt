package com.shall_we

import android.app.Application
import com.kakao.sdk.common.KakaoSdk

class App : Application() {

    companion object{
        lateinit var instance: App
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        KakaoSdk.init(this, getString(R.string.kakao_app_key))
    }

}