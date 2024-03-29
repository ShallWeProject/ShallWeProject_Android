package com.shall_we.retrofit

import com.google.gson.JsonElement
import com.shall_we.dto.PopularRes
import com.shall_we.dto.SttCategoryData
import com.shall_we.dto.TokenRefreshRes
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import com.shall_we.login.data.Auth
import com.shall_we.login.data.AuthResponse
import com.shall_we.login.data.AuthSignOutResponse
import com.shall_we.dto.UpdateReservationReq
import com.shall_we.dto.UserDetail
import com.shall_we.dto.catergoryResponse
import com.shall_we.login.data.LoginGoogleReq
import com.shall_we.login.data.LoginGoogleRes
import com.shall_we.login.data.UserInactiveReq
import com.shall_we.login.signup.UserData
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface IRetrofit {

    @GET(API.EXPERIENCE_GIFT_STTCATEGORY)
    fun experienceGiftSttCategory(@Path("SttCategoryId") categoryId : Int, @Query("category") category : String) : Call<SttCategoryData>

    @GET(API.EXPERIENCE_GIFT_EXPCATEGORY)
    fun experienceGiftExpCategory(@Path("ExpCategoryId") categoryId : Int, @Query("category") category : String) : Call<catergoryResponse>

    @GET(API.EXPERIENCE_GIFT_SEARCH)
    fun experienceGiftSearch(@Query("title") title : String) : Call<PopularRes>

    @GET(API.USERS_GIFT_SEND)
    fun usersGiftSend() : Call<JsonElement>

    @GET(API.USERS_GIFT_RECEIVE)
    fun usersGiftReceive() : Call<JsonElement>

    @GET(API.MEMORY_PHOTO)
    fun getMemoryPhoto(@Query("date") date: String) : Call<JsonElement>

    @POST(API.MEMORY_PHOTO)
    fun postMemoryPhoto(@Body uploadPhotoArray: UploadPhotoArray) : Call<JsonElement>

    @PATCH(API.MEMORY_PHOTO)
    fun deleteMemoryPhoto(@Query("memory-photo-url") photoUrl: String) : Call<JsonElement>

    @DELETE(API.DELETE_RESERVATION)
    fun deleteReservation(@Header("id") id: Int): Call<JsonElement>

    @PUT(API.DELETE_RESERVATION)
    fun putUpdateReservation(@Body updateReservationReq: UpdateReservationReq): Call<JsonElement>

    @POST(API.AUTH_SIGN_IN)
    fun authSignIn(@Body auth: Auth): Call<AuthResponse>

    @POST(API.AUTH_SIGN_OUT)
    fun authSignOut(@Body refreshTokenArray :RefreshTokenArray): Call<AuthSignOutResponse>

    @POST(API.AUTH_REFRESH)
    suspend fun tokenRefresh(@Body refreshTokenArray: RefreshTokenArray): Response<TokenRefreshRes>

    @POST(API.SEND_ONE)
    fun sendOne(@Body phoneNumber: SendOneArray): Call<JsonElement>

    @POST(API.VALID_VERIFICATION)
    fun validVerification(@Body validVerificationArray : ValidVerificationArray): Call<JsonElement>

    @GET(API.EXPERIENCE_GIFT_POPULAR)
    fun experienceGiftPopular() : Call<PopularRes>

    @PATCH(API.USERS)
    fun usersPatch(@Body userData: UserData) : Call<JsonElement>

    @GET(API.USERS)
    fun getUserInfo() : Call<UserDetail>

    @POST("/default/presignedURL-lambda")
    fun getImgUrl(@Body data: BodyData): Call<JsonElement>
  
    @PUT
    fun uploadImg(@Url url: String, @Body image: RequestBody): Call<JsonElement>

    // 유저 회원탈퇴
    @POST(API.USER_INACTIVE)
    fun userInactive(@Body complain : UserInactiveReq) : Call<JsonElement>

    @POST("oauth2/v4/token")
    suspend fun fetchGoogleAuthInfo(
        @Body request: LoginGoogleReq
    ): Response<LoginGoogleRes>?
}

data class ValidVerificationArray(val verificationCode: String, val phoneNumber:String)

data class SendOneArray(val receivePhoneNumber: String)

data class RefreshTokenArray(val refreshToken:String)

data class UploadPhotoArray(val memoryPhotoImgKey: String, val reservationId: Int)

data class BodyData(val ext: String, val dir: String, val filename: String)
