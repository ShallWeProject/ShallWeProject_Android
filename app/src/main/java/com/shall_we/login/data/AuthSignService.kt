package com.shall_we.login.data

import com.google.gson.Gson
import com.shall_we.retrofit.API
import com.shall_we.retrofit.IRetrofit
import com.shall_we.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthSignService(val authSignInterface: IAuthSign) {
    private val iRetrofit : IRetrofit? = RetrofitClient.getClient(API.BASE_URL)?.create(IRetrofit::class.java)

    fun tryPostAuthSignIn(auth: Auth){
        iRetrofit?.authSignIn(AuthLogin(auth.providerId,auth.email))?.enqueue(object:
            Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                if(response.code() == 200){
                    authSignInterface.onPostAuthSignInSuccess(response.body() as AuthResponse)
                }else{
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
    fun tryPostAuthSignUp(auth: Auth){
        iRetrofit?.authSignUp(auth)?.enqueue(object: Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                if(response.code() == 200){
                    authSignInterface.onPostAuthSignUpSuccess(response.body() as AuthResponse)
                }else{
                    try{
                        val gson = Gson()
                        val errorResponse =
                            gson.fromJson(response.errorBody()?.string(), ErrorResponse::class.java)
                        authSignInterface.onPostAuthSignUpFailed(errorResponse.message)
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