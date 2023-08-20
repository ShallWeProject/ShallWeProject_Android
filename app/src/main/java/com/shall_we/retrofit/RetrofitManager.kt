package com.shall_we.retrofit

import android.util.Log
import com.google.gson.JsonElement
import com.shall_we.home.ProductData
import com.shall_we.signup.UserData
import retrofit2.Call
import retrofit2.Response

class RetrofitManager {
    companion object{
        val instance = RetrofitManager()
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
                Log.d("retrofit","RetrofitManager - onResponse() called / response : ${response.code()}")

                when(response.code()){
                    200 -> {
                        response.body()?.let{
                            var parsedProductDataArray = ArrayList<ProductData>()
                            val body = it.asJsonObject
                            val data = body.getAsJsonArray("data")

                            data.forEach { resultItem ->
                                val resultItemObject = resultItem.asJsonObject
                                val title : String = resultItemObject.get("title").asString
                                val subtitle : String = resultItemObject.get("subtitle").asString
                                val price : String = resultItemObject.get("price").asString
                                val formattedPrice = String.format("%,d", price.toInt())

                                val img : String = resultItemObject.get("giftImgUrl").asString
                                val giftid : Int = resultItemObject.get("experienceGiftId").asInt

                                val productItem = ProductData(title = title, subtitle = subtitle, price = formattedPrice, img = img, giftid = giftid)

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
                Log.d("retrofit","RetrofitManager - onResponse() called / response : ${response.code()}")

                when(response.code()){
                    200 -> {
                        response.body()?.let{
                            var parsedProductDataArray = ArrayList<ProductData>()
                            val body = it.asJsonObject
                            val data = body.getAsJsonArray("data")

                            data.forEach { resultItem ->
                                val resultItemObject = resultItem.asJsonObject
                                val title : String = resultItemObject.get("title").asString
                                val subtitle : String = resultItemObject.get("subtitleTitle").asString
                                val price : String = resultItemObject.get("price").asString
                                val formattedPrice = String.format("%,d", price.toInt())

                                val img : String = resultItemObject.get("giftImgUrl").asString
                                val giftid : Int = resultItemObject.get("experienceGiftId").asInt

                                val productItem = ProductData(title = title, subtitle = subtitle, price = formattedPrice, img = img, giftid = giftid)
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

    fun experienceGiftSearch(title: String, completion:(RESPONSE_STATE,ArrayList<ProductData>?) -> Unit){
        val call = iRetrofit?.experienceGiftSearch(title = title) ?:return

        call.enqueue(object : retrofit2.Callback<JsonElement>{
            // 응답 성공
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                Log.d("retrofit","RetrofitManager - onResponse() called / response : ${response.code()}")

                when(response.code()){
                    200 -> {
                        response.body()?.let{
                            var parsedProductDataArray = ArrayList<ProductData>()
                            val body = it.asJsonObject
                            val data = body.getAsJsonArray("data")

                            data.forEach { resultItem ->
                                val resultItemObject = resultItem.asJsonObject
                                val title : String = resultItemObject.get("title").asString
                                val subtitle : String = resultItemObject.get("subtitle").asString
                                val price : String = resultItemObject.get("price").asString
                                val formattedPrice = String.format("%,d", price.toInt())

                                val img : String = resultItemObject.get("giftImgUrl").asString
                                val giftid : Int = resultItemObject.get("experienceGiftId").asInt

                                val productItem = ProductData(title = title, subtitle = subtitle, price = formattedPrice, img = img, giftid = giftid)

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

    fun experienceGiftPopular(completion:(RESPONSE_STATE,ArrayList<ProductData>?) -> Unit){
        val call = iRetrofit?.experienceGiftPopular() ?:return

        call.enqueue(object : retrofit2.Callback<JsonElement>{
            // 응답 성공
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                Log.d("retrofit","RetrofitManager1 - onResponse() called / response : ${response.code()}")

                when(response.code()){
                    200 -> {
                        response.body()?.let{
                            var parsedProductDataArray = ArrayList<ProductData>()
                            val body = it.asJsonObject
                            val data = body.getAsJsonArray("data")

                            data.forEach { resultItem ->
                                val resultItemObject = resultItem.asJsonObject
                                val title : String = resultItemObject.get("title").asString
                                val subtitle : String = resultItemObject.get("subtitle").asString
                                val price : String = resultItemObject.get("price").asString
                                val formattedPrice = String.format("%,d", price.toInt())
                                val img : String = resultItemObject.get("giftImgUrl").asString
                                val giftid : Int = resultItemObject.get("experienceGiftId").asInt

                                val productItem = ProductData(title = title, subtitle = subtitle, price = formattedPrice, img = img, giftid = giftid)
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


    fun usersPatch(userData: UserData, completion:(RESPONSE_STATE) -> Unit){
        val call = iRetrofit?.usersPatch(userData = userData) ?:return

        call.enqueue(object : retrofit2.Callback<JsonElement>{
            // 응답 성공
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                Log.d("retrofit","RetrofitManager - onResponse() called / response : ${response.code()}")

                when(response.code()){
                    200 -> {
                        response.body()?.let{
                            val body = it.asJsonObject
                            val message = body.get("message").asString

                            completion(RESPONSE_STATE.OKAY)
                        }
                    }
                }
            }

            // 응답 실패
            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Log.d("retrofit","RetrofitManager - onFailure() called / t: $t")
                completion(RESPONSE_STATE.FAIL)

            }

        })
    }


}