package com.shall_we.giftExperience

import com.google.gson.JsonElement
import com.shall_we.ExperienceDetail.ExperienceDetailService
import com.shall_we.dto.ExperienceReq
import com.shall_we.dto.ReservationRequest
import com.shall_we.retrofit.API
import com.shall_we.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import java.util.Objects

object ReservationService {
    interface ReservationInterface{
        //예약 정보불러오기
        @GET(API.RESERVATIONS)
        fun get_reservation() : Call<JsonElement>

        //예약정보불러오기
       // @GET(API.RESERVATIONS_USER)
       // fun get_reservation_user() : Call<JsonElement>
        //해당~
        @GET(API.RESERVATIONS_GIFT)
        fun get_reservation_gift(@Header("experienceGiftId") experienceGiftId: Int): Call<JsonElement>


        //해당경험선물에 생성된 예약생성
        @POST(API.RESERVATIONS)
        fun set_experience_gift(
            @Body reservationRequest: ReservationRequest
        ): Call<JsonElement>

    }

    val  reservationService: ReservationService.ReservationInterface?=RetrofitClient.getClient(API.BASE_URL)?.create(ReservationService.ReservationInterface::class.java)
}