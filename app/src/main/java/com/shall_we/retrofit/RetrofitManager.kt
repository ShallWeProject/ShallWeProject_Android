package com.shall_we.retrofit

import android.util.Log
import com.google.gson.JsonElement
import com.shall_we.home.ProductData
import com.shall_we.retrofit.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.create

class RetrofitManager {
    companion object{
        val instace = RetrofitManager()
    }

    // http 콜 만들기
    // 레트로핏 인터페이스 가져오기
    private val iRetrofit : IRetrofit? = RetrofitClient.getClient(API.BASE_URL)?.create(IRetrofit::class.java)

    // 상황별 추천경험 조회 api
    fun experienceGiftSttCategory(categoryId : Int, category: String, completion:(RESPONSE_STATE,ArrayList<ProductData>?) -> Unit){
        val call = iRetrofit?.experienceGiftSttCategory(categoryId = categoryId, category = category) ?:return

        call.enqueue(object : retrofit2.Callback<JsonElement>{
            // 응답 성공
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                Log.d("retrofit","RetrofitManager - onResponse() called / response : ${response.body()}")

                when(response.code()){
                    200 -> {
                        response.body()?.let{
                            var parsedProductDataArray = ArrayList<ProductData>()
                            val body = it.asJsonObject
                            val data = body.getAsJsonArray("data")

                            data.forEach { resultItem ->
                                val resultItemObject = resultItem.asJsonObject
//                                val subtitleArray = resultItemObject.get("subtitle").asJsonObject
                                val title : String = resultItemObject.get("title").asString
//                                val subtitle : String = resultItemObject.get("subtitle").asString
//                                val price : String = resultItemObject.get("price").asString
//                                val img : String = resultItemObject.get("thumbnail").asString
                                val giftid : Int = resultItemObject.get("experienceGiftId").asInt

                                val productItem = ProductData(title = title, subtitle = "subtitle", price = "75,000", img = "img", giftid = giftid)

                                parsedProductDataArray.add(productItem)
                            }
                            completion(RESPONSE_STATE.OKAY,parsedProductDataArray)
                        }
                    }
                }
            }

            // 응답 실패
            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Log.d("retrofit","RetrofitManager - onFailure() called / t: $t")
                completion(RESPONSE_STATE.FAIL, null)
            }

        })
    }

    // 카테고리별 경험 조회 api
    fun experienceGiftExpCategory(categoryId : Int, category: String, completion:(RESPONSE_STATE,ArrayList<ProductData>?) -> Unit){
        val call = iRetrofit?.experienceGiftExpCategory(categoryId = categoryId, category = category) ?:return

        call.enqueue(object : retrofit2.Callback<JsonElement>{
            // 응답 성공
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                Log.d("retrofit","RetrofitManager - onResponse() called / response : ${response.body()}")

                when(response.code()){
                    200 -> {
                        response.body()?.let{
                            var parsedProductDataArray = ArrayList<ProductData>()
                            val body = it.asJsonObject
                            val data = body.getAsJsonArray("data")

                            data.forEach { resultItem ->
                                val resultItemObject = resultItem.asJsonObject
                                val subtitleArray = resultItemObject.get("subtitle").asJsonObject
                                val title : String = resultItemObject.get("title").asString
                                val subtitle : String = subtitleArray.get("title").asString
//                                val price : String = resultItemObject.get("price").asString
                                val img : String = resultItemObject.get("thumbnail").asString
                                val giftid : Int = resultItemObject.get("experienceGiftId").asInt

                                val productItem = ProductData(title = title, subtitle = subtitle, price = "75,000", img = img, giftid = giftid)

                                parsedProductDataArray.add(productItem)
                            }
                            completion(RESPONSE_STATE.OKAY,parsedProductDataArray)
                        }
                    }
                }
            }

            // 응답 실패
            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Log.d("retrofit","RetrofitManager - onFailure() called / t: $t")
                completion(RESPONSE_STATE.FAIL, null)

            }

        })
    }

}