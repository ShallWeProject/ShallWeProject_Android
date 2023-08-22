package com.shall_we.login.data

import android.util.Log
import com.google.gson.Gson
import com.shall_we.retrofit.API
import com.shall_we.retrofit.IRetrofit
import com.shall_we.retrofit.RESPONSE_STATE
import com.shall_we.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthSignService(val authSignInterface: IAuthSign) {
    private val iRetrofit : IRetrofit? = RetrofitClient.getClient(API.BASE_URL)?.create(IRetrofit::class.java)

    fun postAuthSignIn(auth: Auth){
        iRetrofit?.authSignIn(AuthLogin(auth.providerId,auth.email))?.enqueue(object:
            Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                if(response.code() == 200){
                    val authResponse = response.body()
                    if (authResponse != null) {
                        Log.e("login", "Success: ${authResponse}")
                        authSignInterface.onPostAuthSignInSuccess(authResponse)
                    } else {
                        authSignInterface.onPostAuthSignInFailed("응답 데이터를 받아올 수 없습니다.")
                    }
                }
                else if(response.code() == 400){
                    val gson = Gson()
                    val errorResponse =
                        gson.fromJson(response.errorBody()?.string(), ErrorResponse::class.java)
                    authSignInterface.onPostAuthSignInFailed(errorResponse.message)
                    Log.e("login", "Request failed with response code: ${errorResponse.message}")

                }
                else{
                    try{
                        val gson = Gson()
                        val errorResponse =
                            gson.fromJson(response.errorBody()?.string(), ErrorResponse::class.java)
                        authSignInterface.onPostAuthSignInFailed(errorResponse.message)
                    }catch(e:Exception){
                        authSignInterface.onPostAuthSignInFailed(e.message?:"통신 오류")
                    }
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                authSignInterface.onPostAuthSignInFailed(t.message?:"통신 오류")
            }

        })
    }
    fun postAuthSignUp(auth: Auth){
        iRetrofit?.authSignUp(Auth(auth.providerId,auth.nickname,auth.email,auth.profileImgUrl))?.enqueue(object: Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                if(response.code() == 200){
                    val authResponse = response.body()
                    if (authResponse != null) {
                        Log.e("login", "Success: ${authResponse}")
                        authSignInterface.onPostAuthSignUpSuccess(authResponse)
                    } else {
                        authSignInterface.onPostAuthSignUpFailed("응답 데이터를 받아올 수 없습니다.")
                    }
                }
                else if(response.code() == 400){
                    Log.e("login", "singup failed")
                    authSignInterface.onPostAuthSignUpFailed("로그인")
                }
                else{
                    try{
                        val gson = Gson()
                        val errorResponse =
                            gson.fromJson(response.errorBody()?.string(), ErrorResponse::class.java)
                        authSignInterface.onPostAuthSignUpFailed("회원가입실패")
                    }catch(e:Exception){
                        authSignInterface.onPostAuthSignUpFailed(e.message?:"통신 오류")
                    }
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                authSignInterface.onPostAuthSignUpFailed(t.message?:"통신 오류")
            }

        })
    }

}