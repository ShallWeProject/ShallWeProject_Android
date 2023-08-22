package com.shall_we.retrofit

import android.content.Context
import android.util.Log
import com.shall_we.App
import com.shall_we.App.Companion.sharedPreferences
import com.shall_we.login.data.AuthResponse
import com.shall_we.login.data.AuthSignService
import com.shall_we.utils.initProductRecycler
import com.shall_we.utils.isJsonArray
import com.shall_we.utils.isJsonObject
import okhttp3.Authenticator
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.lang.Exception

// 싱글턴
object RetrofitClient {
    // 레트로핏 클라이언트 선언
    var retrofitClient : Retrofit? = null
    // Interceptor를 사용하여 Bearer Token을 헤더에 추가
    private val authInterceptor = Interceptor { chain ->
        val originalRequest = chain.request()
         val token : String? = App.accessToken
//        val token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxMSIsImlhdCI6MTY5MjYxNzY4OCwiZXhwIjoxNjkyNjIxMjg4fQ.srN2yNt0-CwgKsf2kH1IfUaH-5FSF-9CjbbuMYtRpNzsxb0BKExMn_rYMjU7ZIiI-8YmVmfHDX4C0o0U6evaGg"


        val modifiedRequest = originalRequest.newBuilder()
            .header("Authorization", "Bearer $token")
            .build()
        chain.proceed(modifiedRequest)
    }
    // 레트로핏 클라이언트 가져오기
    fun getClient(baseUrl : String): Retrofit? {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY // 요청 및 응답 바디를 포함한 모든 정보를 로그로 출력
        }
        if(retrofitClient == null){
            val httpClient = OkHttpClient.Builder()
                .addInterceptor(authInterceptor)
                .addInterceptor(loggingInterceptor)
                .authenticator(TokenAuthenticator())

                .build()

            retrofitClient = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                //.client(client.build())
                .build()
        }
        return retrofitClient
    }

}
class TokenAuthenticator: Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        val context = App.context
        Log.i("Authenticator", response.toString())
        Log.i("Authenticator", "토큰 재발급 시도 ${App.refreshToken}")
        val refreshTokenArray = RefreshTokenArray(App.refreshToken!!)
        var newAccessToken : String? = null
        return try {
            RetrofitManager.instance.tokenRefresh(refreshToken = refreshTokenArray, completion = { responseState, responseBody ->
                when (responseState) {
                    RESPONSE_STATE.OKAY -> {
                        Log.d("retrofit", "category api : ${responseBody}")
                        val sharedPref = context.getSharedPreferences("MY_APP_PREFS", Context.MODE_PRIVATE)
                        val accessToken = responseBody?.accessToken
                        sharedPref?.edit()?.putString("ACCESS_TOKEN", accessToken)?.apply()
                        val refreshToken = responseBody?.refreshToken
                        sharedPref?.edit()?.putString("REFRESH_TOKEN", refreshToken)?.apply()

                        App.accessToken = sharedPref?.getString("ACCESS_TOKEN", null)
                        App.refreshToken = sharedPref?.getString("REFRESH_TOKEN", null)
                    }

                    RESPONSE_STATE.FAIL -> {
                        Log.d("retrofit", "api 호출 에러")

                    }
                }

                Log.i("Authenticator", "토큰 재발급 성공 : $newAccessToken")

            })

            response.request.newBuilder().removeHeader("Authorization").apply {
                newAccessToken?.let { addHeader("Authorization", "Bearer $it") }
                }.build() // 토큰 재발급이 성공했다면, 기존 헤더를 지우고, 새로운 해더를 단다.
        } catch (e: Exception) {
            e.printStackTrace()
            null // 만약 토큰 재발급이 실패했다면 헤더에 아무것도 추가하지 않는다.
        }
    }
}