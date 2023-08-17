package com.shall_we.retrofit

import com.google.gson.JsonElement
import com.shall_we.dto.ExperienceGiftDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface IRetrofit {

    @GET(API.EXPERIENCE_GIFT_STTCATEGORY)
    fun experienceGiftSttCategory(@Path("SttCategoryId") categoryId : Int, @Query("category") category : String) : Call<JsonElement>

    @GET(API.EXPERIENCE_GIFT_EXPCATEGORY)
    fun experienceGiftExpCategory(@Path("ExpCategoryId") categoryId : Int, @Query("category") category : String) : Call<JsonElement>

    @GET(API.EXPERIENCE_GIFT_SEARCH)
    fun experienceGiftSearch(@Query("title") title : String) : Call<JsonElement>



}