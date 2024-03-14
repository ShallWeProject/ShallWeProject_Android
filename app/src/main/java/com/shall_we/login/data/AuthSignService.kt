package com.shall_we.login.data

import android.util.Log
import com.google.gson.Gson
import com.shall_we.BuildConfig
import com.shall_we.R
import com.shall_we.retrofit.API
import com.shall_we.retrofit.IRetrofit
import com.shall_we.retrofit.RESPONSE_STATE
import com.shall_we.retrofit.RefreshTokenArray
import com.shall_we.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create

class AuthSignService(val authSignInterface: IAuthSign) {
    private val iRetrofit : IRetrofit? = RetrofitClient.getClient(API.BASE_URL)?.create(IRetrofit::class.java)
    private val iRetrofitGoogle : IRetrofit? = RetrofitClient.provideRetrofit("https://www.googleapis.com/")?.create(IRetrofit::class.java)
    fun postAuthSignIn(auth: Auth){
        iRetrofit?.authSignIn(Auth(providerId = auth.providerId, provider = auth.provider, email = auth.email, refreshToken = auth.refreshToken))?.enqueue(object:
            Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                if(response.code() == 200){
                    val authResponse = response.body()
                    if (authResponse != null) {
                        Log.e("login", "Success: ${authResponse}")
                        if(authResponse.data.isSignUpComplete == true){
                            authSignInterface.onPostAuthSignInSuccess(authResponse)
                        }
                        else{
                            authSignInterface.onPostAuthSignUpSuccess(authResponse)
                        }
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

    suspend fun fetchGoogleAuthInfo(
        authCode: String
    ): LoginGoogleResult<LoginGoogleRes> {
        iRetrofitGoogle?.fetchGoogleAuthInfo(
            LoginGoogleReq(
                grant_type = "authorization_code",
                client_id = BuildConfig.GOOGLE_CLIENT_ID,
                client_secret = BuildConfig.GOOGLE_CLIENT_SECRET,
                redirect_uri = "https://shallwes.com",
                code = authCode
            )
        )?.run {
            return LoginGoogleResult.Success(this.body() ?: LoginGoogleRes())
        } ?: return LoginGoogleResult.Error(Exception("Retrofit Exception"))
    }
}