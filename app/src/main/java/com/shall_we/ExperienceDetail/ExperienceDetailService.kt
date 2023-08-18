package com.shall_we.ExperienceDetail

import com.shall_we.dto.ExperienceGiftDto
import com.shall_we.dto.ExperienceMainRes
import com.shall_we.dto.ExperienceReq
import com.shall_we.retrofit.API
import com.shall_we.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

object ExperienceDetailService {

    interface ExperienceDetailInterface{
        //경험 메인
        @GET(API.EXPERIENCE_GIFT)
        fun get_experience_gift() : Call<ExperienceMainRes>

        //경험상세
        @GET(API.EXPERIENCE_GIFT_EXPERIENCE_ID)
        fun  get_experience_detail_data(@Path("ExperienceGiftId") ExperienceGiftId: Int): Call<ExperienceGiftDto>

        //경험선물추가
        @POST(API.EXPERIENCE_GIFT)
        fun set_experience_gift(
            @Body ExperienceReq: ExperienceReq
        ): Call<ExperienceReq>
    }

   // val experienceDetailService =
     //   retrofitClient?.create(ExperienceDetailService.ExperienceDetailInterface::class.java)

    val  experienceDetailService: ExperienceDetailInterface?=RetrofitClient.getClient(API.BASE_URL)?.create(ExperienceDetailService.ExperienceDetailInterface::class.java)


}