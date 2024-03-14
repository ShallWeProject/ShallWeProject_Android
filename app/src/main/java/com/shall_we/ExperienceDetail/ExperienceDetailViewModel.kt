package com.shall_we.ExperienceDetail

import ExperienceDetailRes
import GiftDTO
import android.util.Log
import androidx.lifecycle.ViewModel
import com.shall_we.retrofit.RESPONSE_STATE
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ExperienceDetailViewModel:ViewModel() {
    private var experienceDetailData: GiftDTO? = null

    fun getExperienceDetailData(experienceGiftId: Int, completion: (RESPONSE_STATE, GiftDTO?) -> Unit) {
        // 이미 데이터가 저장되어 있는 경우, 저장된 데이터를 반환
        if (experienceDetailData != null) {
            completion(RESPONSE_STATE.OKAY, experienceDetailData)
            return
        }

        // 데이터가 저장되어 있지 않은 경우, API 호출하여 데이터를 받아오고 저장
        ExperienceDetailService.experienceDetailService?.get_experience_detail_data(experienceGiftId)
            ?.enqueue(object : Callback<ExperienceDetailRes> {
                override fun onResponse(
                    call: Call<ExperienceDetailRes>,
                    response: Response<ExperienceDetailRes>
                ) {
                    if (response.isSuccessful) {
                        val data = response.body()?.data
                        experienceDetailData = data // 받아온 데이터를 저장
                        completion(RESPONSE_STATE.OKAY, data)
                    } else {
                        completion(RESPONSE_STATE.FAIL, null)
                    }
                }

                override fun onFailure(call: Call<ExperienceDetailRes>, t: Throwable) {
                    Log.d("whatisthis", "get_experience_detail_data, 네트워크 오류가 발생했습니다. " + t.message.toString())
                    completion(RESPONSE_STATE.FAIL, null)
                }
            })
    }

}





