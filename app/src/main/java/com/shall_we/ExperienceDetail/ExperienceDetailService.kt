package com.shall_we.ExperienceDetail

import ExperienceDetailRes
import com.google.gson.JsonElement
import com.shall_we.retrofit.API
import com.shall_we.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

object ExperienceDetailService {

    interface ExperienceDetailInterface{
        //경험 메인
        @GET(API.EXPERIENCE_GIFT)
        fun get_experience_gift() : Call<JsonElement>

        //경험상세
        @GET(API.EXPERIENCE_GIFT_EXPERIENCE_ID)
        fun get_experience_detail_data(@Path("ExperienceGiftId") ExperienceGiftId: Int): Call<ExperienceDetailRes>

       // 예약추가
//        @POST(API.RESERVATIONS)
//        fun set_experience_gift(
//            @Body ReservationRequest: ReservationRequest
//        ): Call<JsonElement>
    }

   // val experienceDetailService =
     //   retrofitClient?.create(ExperienceDetailService.ExperienceDetailInterface::class.java)

    val  experienceDetailService: ExperienceDetailInterface?=RetrofitClient.getClient(API.BASE_URL)?.create(ExperienceDetailService.ExperienceDetailInterface::class.java)


}