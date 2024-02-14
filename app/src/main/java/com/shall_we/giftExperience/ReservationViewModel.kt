package com.shall_we.giftExperience

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.JsonElement
import com.shall_we.dto.ExperienceExpCategoryRes
import com.shall_we.dto.Reservation
import com.shall_we.dto.ReservationItem
import com.shall_we.dto.ReservationRequest
import com.shall_we.dto.SttCategoryRes
import com.shall_we.dto.ValidTimeRes
import com.shall_we.retrofit.RESPONSE_STATE
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReservationViewModel:ViewModel() {

    private var _reservation_data = MutableLiveData<JsonElement>()
    val reservation_data: LiveData<JsonElement> get() = _reservation_data


    fun get_reservation(completion:(RESPONSE_STATE, ArrayList<Reservation>?) -> Unit) {
        ReservationService.reservationService?.get_reservation()?.enqueue(object :
            Callback<JsonElement> {
            override fun onResponse(
                call: Call<JsonElement>,
                response: Response<JsonElement>
            ) {
                if (response.isSuccessful) {
                    Log.d("get", "reservation, response 성공")
                    //_experience_gift_data.value = (response.body())

                    response.body()?.let {

                        Log.d("whattt",it.toString())
                    }

                } else {
                    // _experience_gift_data.postValue(ExperienceGiftDto())
                    Log.d("whatisthis", "reservation, response 못받음")
                }
            }

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                // _experience_gift_data.value = ExperienceMainRes(emptyList(), emptyList())
                Log.d("whatisthis", "_experience_gift_data, 호출 실패: ${t.localizedMessage}")
            }
        })
    }

    fun get_reservation_gift(experienceGiftId: Int, completion:(RESPONSE_STATE, ArrayList<Reservation>?) -> Unit) {
        ReservationService.reservationService?.get_reservation_gift(experienceGiftId)?.enqueue(object :
            Callback<JsonElement> {
            override fun onResponse(
                call: Call<JsonElement>,
                response: Response<JsonElement>
            ) {
                if (response.isSuccessful) {
                    Log.d("whatisthis", "reservation_gift, response 성공")
                    //_experience_gift_data.value = (response.body())

                    response.body()?.let {

                        Log.d("whattt",it.toString())
                    }

                } else {
                    // _experience_gift_data.postValue(ExperienceGiftDto())
                    Log.d("whatisthis", "reservation, response 못받음")
                }
            }

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                // _experience_gift_data.value = ExperienceMainRes(emptyList(), emptyList())
                Log.d("whatisthis", "_experience_gift_data, 호출 실패: ${t.localizedMessage}")
            }
        })
    }

    fun set_experience_gift(reservationRequest: ReservationRequest, completion: (RESPONSE_STATE, Int?) -> Unit) {
        ReservationService.reservationService?.set_experience_gift(
            ReservationRequest(
                reservationRequest.experienceGiftId,
                reservationRequest.persons,
                reservationRequest.date,
                //reservationRequest.receiverName,
                reservationRequest.time,
                reservationRequest.phoneNumber,
                reservationRequest.imageKey,
                reservationRequest.invitationComment
                     //   reservationRequest.reservationStatus
           ))?.enqueue(object :
            Callback<JsonElement> {
            override fun onResponse(
                call: Call<JsonElement>,
                response: Response<JsonElement>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        Log.d("set_experience_gift", it.toString())
                        completion(RESPONSE_STATE.OKAY, response.code())
                    }
                } else {
                    // 실패한 응답 코드와 메시지 출력
                    Log.d("set_experience_gift", "Error code: ${response.code()}, error message: ${response.message()}")
                    completion(RESPONSE_STATE.OKAY, response.code())

                }
            }

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                // 상세한 오류 메시지 출력
                Log.d("whatisthis", "_experience_gift_data, 호출 실패: ${t.localizedMessage}")
                completion(RESPONSE_STATE.FAIL, null)

            }
        })
    }

}