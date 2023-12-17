package com.shall_we.ExperienceDetail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.JsonElement
import com.shall_we.dto.ExperienceDetailRes
import com.shall_we.dto.ExperienceGiftDto
import com.shall_we.dto.ExperienceMainRes
import com.shall_we.dto.GiftDTO
import com.shall_we.dto.ReservationRequest
import com.shall_we.retrofit.RESPONSE_STATE
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ExperienceDetailViewModel:ViewModel() {



    //경험 상세
    private var _experience_detail_data = MutableLiveData<ExperienceGiftDto>()
    val experience_detail_data: LiveData<ExperienceGiftDto> get() = _experience_detail_data
    //경험 메인
    private var _experience_gift_data = MutableLiveData<ExperienceMainRes>()
    val experience_gift_data: LiveData<ExperienceMainRes> get() = _experience_gift_data
    //경험 선물 추가


    private val _update_gift_data = MutableLiveData<JsonElement?>()
    val updateGiftData: LiveData<JsonElement?> get() = _update_gift_data

    fun get_experience_gift() {
        ExperienceDetailService.experienceDetailService?.get_experience_gift()?.enqueue(object :
            Callback<JsonElement> {
            override fun onResponse(
                call: Call<JsonElement>,
                response: Response<JsonElement>
            ) {
                if (response.isSuccessful) {
                    Log.d("what?",response.toString())
                }
            }

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {

                Log.d("whatisthis", "_experience_gift_data, 호출 실패: ${t.localizedMessage}")
            }
        })
    }
    fun get_experience_detail_data(
        ExperienceGiftId: Int,
        completion: (RESPONSE_STATE, GiftDTO?) -> Unit
    ) {
        ExperienceDetailService.experienceDetailService?.get_experience_detail_data(ExperienceGiftId)
            ?.enqueue(object : Callback<ExperienceDetailRes> {
                override fun onResponse(
                    call: Call<ExperienceDetailRes>,
                    response: Response<ExperienceDetailRes>
                ) {
                    if (response.isSuccessful ) {
                        completion(RESPONSE_STATE.OKAY, response.body()?.data)
                    }
                }

                override fun onFailure(call: Call<ExperienceDetailRes>, t: Throwable) {
                    Log.d(
                        "whatisthis",
                        "get_experience_detail_data, 네트워크 오류가 발생했습니다. " + t.message.toString()
                    )
                    completion(RESPONSE_STATE.FAIL, null)
                }
            })
    }

//    fun set_experience_gift(reservationRequest: ReservationRequest) {
//        ExperienceDetailService.experienceDetailService
//            ?.set_experience_gift(reservationRequest)
//            ?.enqueue(object : Callback<JsonElement> {
//
//                override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
//                    if (response.isSuccessful) {
//                        Log.d("whatisthis", "_update_gift_data, response 성공")
//                            // _update_gift_data.value = response.body()
//                        Log.d("whatisthis", reservationRequest.toString())
//                    } else {
//                       // _update_gift_data.postValue(reservationRequest)
//                        Log.d("whatisthis", "_update_gift_data, response 못받음")
//                    }
//                }
//
//                override fun onFailure(call: Call<JsonElement>, t: Throwable) {
//                   // _update_gift_data.value = reservationRequest
//                    Log.d("whatisthis", "_update_gift_data, 호출 실패: ${t.localizedMessage}")
//                }
//            })
//    }

}



