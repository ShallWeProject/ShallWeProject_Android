package com.shall_we.ExperienceDetail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.JsonElement
import com.shall_we.dto.ExperienceDetailRes
import com.shall_we.dto.ExperienceGiftDto
import com.shall_we.dto.ExperienceMainRes
import com.shall_we.dto.ExperienceReq
import com.shall_we.dto.ExplanationRes
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
    private var _update_gift_data = MutableLiveData<ExperienceReq>()
    val update_gift_data: LiveData<ExperienceReq> get() = _update_gift_data


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
        completion: (RESPONSE_STATE, ArrayList<ExperienceDetailRes>?) -> Unit
    ) {
        ExperienceDetailService.experienceDetailService?.get_experience_detail_data(ExperienceGiftId)
            ?.enqueue(object : Callback<JsonElement> {
                override fun onResponse(
                    call: Call<JsonElement>,
                    response: Response<JsonElement>
                ) {
                    if (response.isSuccessful) {
                        Log.d("whatisthis", "_experience_detail_data, response 성공")

                        response.body()?.let {
                            val parsedDataArray = ArrayList<ExperienceDetailRes>()
                            val body = it.asJsonObject

                            val data = body.getAsJsonObject("data")

                            val giftImageUrl = data.get("giftImgUrl").asString
                            //val thumbnail = data.get("thumbnail").asString
                            val title = data.get("title").asString
                            val subtitle = data.get("subtitle").asString
                            val price = data.get("price").asInt
                            val description = if (!data.get("description").isJsonNull) data.get("description").asString else null
                            //val experienceCategory = data.get("expCategory").asString
                            //val statementCategory = data.get("sttCategory").asString
                            val experienceGiftId = data.get("experienceGiftId").asInt


                            val explanationResJsonArray = data.getAsJsonArray("explanation")
                            val explanationResList = ArrayList<ExplanationRes>()
                            explanationResJsonArray?.forEach { expItem ->
                                val expItemObject = expItem.asJsonObject
                                val description = expItemObject.get("description").asString
                                val explanationUrl = expItemObject.get("explanationUrl").asString

                                val explanationRes = ExplanationRes(
                                    description = description,
                                    explanationUrl=explanationUrl)
                                explanationResList.add(explanationRes)
                            }

                            val experienceDetailRes = ExperienceDetailRes(
                                giftImageUrl = giftImageUrl,
                                //thumbnail = thumbnail,
                                title = title,
                                subtitle = subtitle,
                                price = price,
                                explanation = explanationResList,
                                description = description,
                                //experienceCategory = experienceCategory,
                                //statementCategory = statementCategory,
                                experienceGiftId = experienceGiftId
                            )

                            parsedDataArray.add(experienceDetailRes)

                            completion(RESPONSE_STATE.OKAY, parsedDataArray)

                        }
                    } else {
                        Log.d("whatisthis", "_experience_detail_data, response 못받음")
                        completion(RESPONSE_STATE.FAIL, null)
                    }
                }

                override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                    Log.d(
                        "whatisthis",
                        "get_experience_detail_data, 네트워크 오류가 발생했습니다. " + t.message.toString()
                    )
                    completion(RESPONSE_STATE.FAIL, null)
                }
            })
    }

    fun set_experience_gift(experienceReq: ExperienceReq) {
        ExperienceDetailService.experienceDetailService?.set_experience_gift(experienceReq)?.enqueue(object :
            Callback<ExperienceReq> {

            override fun onResponse(call: Call<ExperienceReq>, response: Response<ExperienceReq>) {
                if (response.isSuccessful) {
                    Log.d("whatisthis", "_update_gift_data, response 성공")
                    _update_gift_data.value = (response.body())
                } else {
                    _update_gift_data.postValue(experienceReq)
                    Log.d("whatisthis", "_update_gift_data, response 못받음")
                }
            }

            override fun onFailure(call: Call<ExperienceReq>, t: Throwable) {
                _update_gift_data.value = experienceReq
                Log.d("whatisthis", "_update_gift_data, 호출 실패: ${t.localizedMessage}")
            }
        })
    }

}



