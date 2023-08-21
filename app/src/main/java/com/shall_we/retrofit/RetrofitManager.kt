package com.shall_we.retrofit

import android.util.Log
import com.google.gson.JsonElement
import com.shall_we.home.ProductData
import com.shall_we.myAlbum.MyAlbumData
import com.shall_we.mypage.MyGiftData
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

//                                val img : String = resultItemObject.get("giftImgUrl").asString
                                val giftid : Int = resultItemObject.get("experienceGiftId").asInt

                                val productItem = ProductData(title = title, subtitle = subtitle, price = formattedPrice, img = "img", giftid = giftid)

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

//                                val img : String = resultItemObject.get("giftImgUrl").asString
                                val giftid : Int = resultItemObject.get("experienceGiftId").asInt

                                val productItem = ProductData(title = title, subtitle = subtitle, price = formattedPrice, img = "img", giftid = giftid)
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

    fun experienceGiftSearch(title: String, completion:(RESPONSE_STATE, ArrayList<ProductData>?) -> Unit){
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

//                                val img : String = resultItemObject.get("giftImgUrl").asString
                                val giftid : Int = resultItemObject.get("experienceGiftId").asInt

                                val productItem = ProductData(title = title, subtitle = subtitle, price = formattedPrice, img = "img", giftid = giftid)

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

    fun memoryPhoto(date: String, completion:(RESPONSE_STATE, ArrayList<MyAlbumData>?) -> Unit){
        val call = iRetrofit?.memoryPhoto(date=date) ?:return

        call.enqueue(object : retrofit2.Callback<JsonElement>{
            // 응답 성공
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                Log.d("retrofit","RetrofitManager - onResponse() called / response : ${response.code()}")

                when(response.code()){
                    200 -> {
                        response.body()?.let{
                            var parsedMyAlbumDataArray = ArrayList<MyAlbumData>()
                            val body = it.asJsonObject
                            val data = body.getAsJsonArray("data")

                            data.forEach { resultItem ->
                                val resultItemObject = resultItem.asJsonObject
                                val reservationId : Int = resultItemObject.get("reservationId").asInt
                                val mutableList = resultItemObject.getAsJsonArray("memoryPhotoImages")
                                val memoryPhotoImages = mutableListOf<String>()
                                for (i in 0 until mutableList.size()) {
                                    val item = mutableList[i].asString
                                    memoryPhotoImages.add(item)
                                }

//                                val myAlbumItem = MyAlbumData(idx = reservationId, date = date, memoryImgs = memoryPhotoImages)

//                                parsedMyAlbumDataArray.add(myAlbumItem)
                            }
                            completion(RESPONSE_STATE.OKAY,parsedMyAlbumDataArray)
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

    fun usersGiftSend(completion:(RESPONSE_STATE,ArrayList<MyGiftData>?) -> Unit){
        val call = iRetrofit?.usersGiftSend() ?:return

        call.enqueue(object : retrofit2.Callback<JsonElement>{
            // 응답 성공
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                Log.d("retrofit","RetrofitManager - onResponse() called / response : ${response.code()}")

                when(response.code()){
                    200 -> {
                        response.body()?.let{
                            var parsedProductDataArray = ArrayList<MyGiftData>()
                            val body = it.asJsonObject
                            val data = body.getAsJsonArray("data")

                            data.forEach { resultItem ->
                                val resultItemObject = resultItem.asJsonObject
                                val title : String = resultItemObject.get("title").asString
                                val subtitle : String = resultItemObject.get("subtitle").asString
                                val price : String = resultItemObject.get("price").asString
                                val formattedPrice = String.format("%,d", price.toInt())

//                                val img : String = resultItemObject.get("giftImgUrl").asString
                                val giftid : Int = resultItemObject.get("experienceGiftId").asInt

                                //val giftItem = MyGiftData()

                                //parsedProductDataArray.add(giftItem)
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

    fun deleteReservation(id : Int, completion:(RESPONSE_STATE,ArrayList<MyGiftData>?) -> Unit){
        val call = iRetrofit?.experienceGiftSttCategory(id = id) ?:return

        call.enqueue(object : retrofit2.Callback<JsonElement>{
            // 응답 성공
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                Log.d("retrofit","RetrofitManager - onResponse() called / response : ${response.code()}")

                when(response.code()){
                    200 -> {
                        response.body()?.let{
                            var parsedProductDataArray = ArrayList<MyGiftData>()
                            val body = it.asJsonObject
                            val data = body.getAsJsonArray("data")

                            data.forEach { resultItem ->
                                val resultItemObject = resultItem.asJsonObject
                                val id : Int = resultItemObject.get("id").asInt
                                val title : String = resultItemObject.get("title").asString
                                val subtitle : String = resultItemObject.get("subtitle").asString
                                val date : String = resultItemObject.get("date").asString
                                val time : String = resultItemObject.get("time").asString
                                val cancellation : Boolean = resultItemObject.get("cancellation").asBoolean
                                val invitationComment : String = resultItemObject.get("invitationComment").asString


                                val productItem = MyGiftData(idx = id, title = title, description = subtitle, date = date, time = time, cancellation = cancellation, message = invitationComment)
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