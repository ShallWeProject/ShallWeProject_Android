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
    private var retrofitClient : Retrofit? = null
    // Interceptor를 사용하여 Bearer Token을 헤더에 추가
    private val authInterceptor = Interceptor { chain ->
        val originalRequest = chain.request()
        val token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI4IiwiaWF0IjoxNjkxOTMwNDA1LCJleHAiOjE2OTE5MzQwMDV9.g630NNwl30Xhf-8whAP91VuYUDWhovyr_cYsXmeUgqEWmj2GPOMJnjE2iJ40FAwnoUqLFyMAVkd38GLHbKDeRg"
        val modifiedRequest = originalRequest.newBuilder()
            .header("Authorization", "Bearer $token")
            .build()
        chain.proceed(modifiedRequest)
    }
    // 레트로핏 클라이언트 가져오기
    fun getClient(baseUrl : String): Retrofit? {
        Log.d("retrofit", "RetrofitClient - getClient() called")

        if(retrofitClient == null){
            val httpClient = OkHttpClient.Builder()
                .addInterceptor(authInterceptor)
                .build()

            retrofitClient = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
//                .client(client.build())
                .build()
        }
        return retrofitClient
    }

}