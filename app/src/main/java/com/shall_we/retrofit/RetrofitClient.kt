package com.shall_we.retrofit

import android.util.Log
import com.shall_we.utils.isJsonArray
import com.shall_we.utils.isJsonObject
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception

// 싱글턴
object RetrofitClient {
    // 레트로핏 클라이언트 선언
    var retrofitClient : Retrofit? = null
    // Interceptor를 사용하여 Bearer Token을 헤더에 추가
    private val authInterceptor = Interceptor { chain ->
        val originalRequest = chain.request()
        val token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxMCIsImlhdCI6MTY5MjI3ODUxMSwiZXhwIjoxNjkyMjgyMTExfQ.2jnM-SrvJyAXfaTUrO3aaWbdhjT0z_jTwmJYNRHjIHBuZnDelhNMqO2muVwPB1bD1KlX3vVO6-4BB7dJ6CvRxw"
        val modifiedRequest = originalRequest.newBuilder()
            .header("Authorization", "Bearer $token")
            .build()
        chain.proceed(modifiedRequest)
    }
    // 레트로핏 클라이언트 가져오기
    fun getClient(baseUrl : String): Retrofit? {
        if(retrofitClient == null){
            val httpClient = OkHttpClient.Builder()
                .addInterceptor(authInterceptor)
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