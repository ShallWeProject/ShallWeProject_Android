package com.shall_we.ExperienceDetail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shall_we.dto.ExperienceGiftDto
import com.shall_we.dto.ExperienceMainRes
import com.shall_we.dto.ExperienceReq
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
    private var _update_gift_data = MutableLiveData<ExperienceReq>()
    val update_gift_data: LiveData<ExperienceReq> get() = _update_gift_data


    fun get_experience_gift() {
        ExperienceDetailService.experienceDetailService?.get_experience_gift()?.enqueue(object :
            Callback<ExperienceMainRes> {
            override fun onResponse(
                call: Call<ExperienceMainRes>,
                response: Response<ExperienceMainRes>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        _experience_gift_data.value = it
                    } ?: run {
                        _experience_gift_data.value = ExperienceMainRes(emptyList(), emptyList())
                        Log.d("whatisthis", "_experience_gift_data, response body is null")
                    }
                } else {
                    _experience_gift_data.value = ExperienceMainRes(emptyList(), emptyList())
                    Log.d("whatisthis", "_experience_gift_data, response 실패: ${response.errorBody()?.string() ?: "unknown"}")
                }
            }

            override fun onFailure(call: Call<ExperienceMainRes>, t: Throwable) {
                _experience_gift_data.value = ExperienceMainRes(emptyList(), emptyList())
                Log.d("whatisthis", "_experience_gift_data, 호출 실패: ${t.localizedMessage}")
            }
        })
    }
    fun get_experience_detail_data(id: Int) {
        ExperienceDetailService.experienceDetailService?.get_experience_detail_data(id)?.enqueue(object :
            Callback<ExperienceGiftDto> {
            override fun onResponse(
                call: Call<ExperienceGiftDto>,
                response: Response<ExperienceGiftDto>
            ) {
                if (response.isSuccessful) {
                    _experience_detail_data.value = (response.body())
                } else {
                    _experience_detail_data.postValue(ExperienceGiftDto())
                    Log.d("whatisthis", "_experience_detail_data, response 못받음")
                }
            }

            override fun onFailure(call: Call<ExperienceGiftDto>, t: Throwable) {
                Log.d(
                    "whatisthis",
                    " get_experience_detail_data, 네트워크 오류가 발생했습니다." + t.message.toString()
                )
            }
        })
    }

    fun set_experience_gift(experienceReq: ExperienceReq) {
        ExperienceDetailService.experienceDetailService?.set_experience_gift(experienceReq)?.enqueue(object :
            Callback<ExperienceReq> {

            override fun onResponse(call: Call<ExperienceReq>, response: Response<ExperienceReq>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        _update_gift_data.value = it
                    } ?: run {
                        _update_gift_data.value = experienceReq
                        Log.d("whatisthis", "_update_gift_data, response body is null")
                    }
                } else {
                    _experience_gift_data.value = ExperienceMainRes(emptyList(), emptyList())
                    Log.d("whatisthis", "_experience_gift_data, response 실패: ${response.errorBody()?.string() ?: "unknown"}")
                }
            }

            override fun onFailure(call: Call<ExperienceReq>, t: Throwable) {
                _experience_gift_data.value = ExperienceMainRes(emptyList(), emptyList())
                Log.d("whatisthis", "_experience_gift_data, 호출 실패: ${t.localizedMessage}")
            }
        })
    }

}



