package com.shall_we.retrofit

import com.google.gson.JsonElement
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import com.shall_we.login.data.Auth
import com.shall_we.login.data.AuthLogin
import com.shall_we.login.data.AuthResponse
import com.shall_we.login.data.AuthSignOutResponse
import com.shall_we.dto.ExperienceGiftDto
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface IRetrofit {

    @GET(API.EXPERIENCE_GIFT_STTCATEGORY)
    fun experienceGiftSttCategory(@Path("SttCategoryId") categoryId : Int, @Query("category") category : String) : Call<JsonElement>

    @GET(API.EXPERIENCE_GIFT_EXPCATEGORY)
    fun experienceGiftExpCategory(@Path("ExpCategoryId") categoryId : Int, @Query("category") category : String) : Call<JsonElement>

    @GET(API.EXPERIENCE_GIFT_SEARCH)
    fun experienceGiftSearch(@Query("title") title : String) : Call<JsonElement>

    //유저 로그인
    @POST(API.AUTH_SIGN_IN)
    fun authSignIn(@Body auth: AuthLogin): Call<AuthResponse>

    // 유저 회원가입
    @POST(API.AUTH_SIGN_UP)
    fun authSignUp(@Body auth: Auth): Call<AuthResponse>

    // 유저 로그아웃
    @POST(API.AUTH_SIGN_OUT)
    fun authSignOut(@Header("Authorization") accessToken: String?): Call<AuthSignOutResponse>

    // 토큰갱신
    @POST(API.AUTH_REFRESH)
    fun tokenRefresh(@Body refreshToken :String): Call<AuthResponse>

    // 인증 문자 전송
    @POST(API.SEND_ONE)
    fun sendOne(@Body phoneNumber: SendOneArray): Call<JsonElement>

    // 인증 번호 검증
    @POST(API.VALID_VERIFICATION)
    fun validVerification(@Body validVerificationArray : ValidVerificationArray): Call<JsonElement>

    @GET(API.EXPERIENCE_GIFT_POPULAR)
    fun experienceGiftPopular() : Call<JsonElement>

    @PATCH(API.USERS)
    fun usersPatch(@Body userData: com.shall_we.signup.UserData) : Call<JsonElement>

}

data class ValidVerificationArray(val verificationCode: String, val phoneNumber:String)

data class SendOneArray(val phoneNumber: String)