package com.shall_we.retrofit

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.shall_we.home.ProductData
import com.shall_we.signup.UserData
import com.shall_we.login.data.Auth
import com.shall_we.login.data.AuthLogin
import com.shall_we.login.data.AuthResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.shall_we.myAlbum.MyAlbumData
import com.shall_we.mypage.MyGiftData
import java.text.SimpleDateFormat

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

  fun sendOne(phoneNumber: SendOneArray, completion: (RESPONSE_STATE, JsonElement?) -> Unit){
        val call = iRetrofit?.sendOne(phoneNumber) ?: return
        call.enqueue(object : Callback<JsonElement> {
            // 응답 성공인 경우
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                Log.d("retrofit", "RetrofitManager - onResponse() called / response : ${response.message()}")

                when (response.code()) {
                    200 -> {
                        response.body()?.let {
                            completion(RESPONSE_STATE.OKAY, it)
                        }
                    }
                }
            }

            // 응답 실패인 경우
            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Log.d("retrofit", "RetrofitManager - onFailure() called / t: $t")
                completion(RESPONSE_STATE.FAIL, null)
            }
        })
    }

    fun usersPatch(userData: UserData, completion:(RESPONSE_STATE) -> Unit){
        val call = iRetrofit?.usersPatch(userData = userData) ?:return

        call.enqueue(object : retrofit2.Callback<JsonElement> {
            // 응답 성공
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                Log.d(
                    "retrofit",
                    "RetrofitManager - onResponse() called / response : ${response.code()}"
                )

                when (response.code()) {
                    200 -> {
                        response.body()?.let {
                            val body = it.asJsonObject
                            completion(RESPONSE_STATE.OKAY)
                        }
                    }
                }
            }

            // 응답 실패
            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Log.d("retrofit", "RetrofitManager - onFailure() called / t: $t")
                completion(RESPONSE_STATE.FAIL)

            }
        })
        }


    fun validVerification(validVerificationArray: ValidVerificationArray,completion: (RESPONSE_STATE, Int?) -> Unit){
        val call = iRetrofit?.validVerification(validVerificationArray) ?: return
        call.enqueue(object : Callback<JsonElement> {
            // 응답 성공인 경우
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                Log.d(
                    "retrofit",
                    "RetrofitManager - onResponse() called / response : ${response.code()}"
                )
                completion(RESPONSE_STATE.OKAY, response.code())

            }

            // 응답 실패인 경우
            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Log.d("retrofit", "RetrofitManager - onFailure() called / t: $t")
                completion(RESPONSE_STATE.FAIL, null)
            }
        })
    }

    
    fun memoryPhoto(date: String, completion:(RESPONSE_STATE, ArrayList<MyAlbumData>?) -> Unit){
        val call = iRetrofit?.memoryPhoto(date=date) ?:return

        call.enqueue(object : retrofit2.Callback<JsonElement>{
            // 응답 성공
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                Log.d("retrofit","RetrofitManager - memoryPhoto onResponse() called / response : ${response.code()}")

                when(response.code()){
                    200 -> {
                        response.body()?.let{
                            var parsedMyAlbumDataArray = ArrayList<MyAlbumData>()
                            val body = it.asJsonObject
                            val data = body.getAsJsonArray("data")

//                            data.forEach { resultItem ->
//                                val resultItemObject = resultItem.asJsonObject
//                                val reservationId : Int = resultItemObject.get("reservationId").asInt
//                                val mutableList = resultItemObject.getAsJsonArray("memoryPhotoImages")
//                                val memoryPhotoImages = mutableListOf<String>()
//                                for (i in 0 until mutableList.size()) {
//                                    val item = mutableList[i].asString
//                                    memoryPhotoImages.add(item)
//                                }
//
////                                val myAlbumItem = MyAlbumData(idx = reservationId, date = date, memoryImgs = memoryPhotoImages)
//
////                                parsedMyAlbumDataArray.add(myAlbumItem)
//                            }
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
                Log.d("retrofit","RetrofitManager - userGiftSend onResponse() called / response : ${response.code()}")

                when(response.code()){
                    200 -> {
                        response.body()?.let{
                            var parsedProductDataArray = ArrayList<MyGiftData>()
                            val body = it.asJsonObject
                            val data = body.getAsJsonObject("data")
                            val gifts = data.getAsJsonArray("gifts")
                            gifts.forEach { resultItem ->
                                val resultItemObject = resultItem.asJsonObject
                                val reservationId : Int = resultItemObject.get("reservationId").asInt
                                val experienceGift = resultItemObject.getAsJsonObject("experienceGift")
                                val title: String = experienceGift.get("title").asString
                                val subtitle: String = experienceGift.get("subtitle").asString

                                val dateTime : String = resultItemObject.get("dateTime").asString
                                val invitationComment : String = resultItemObject.get("invitationComment").asString

                                val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
                                val dateTimeStr = sdf.parse(dateTime)

                                // Date 객체에서 날짜와 시간 추출
                                val dateFormatter = SimpleDateFormat("yyyy.MM.dd")
                                val timeFormatter = SimpleDateFormat("HH시")

                                val date = dateFormatter.format(dateTimeStr)
                                val time = timeFormatter.format(dateTimeStr)

                                val giftItem = MyGiftData(idx = reservationId, title = title, description = subtitle, date = date, time = time, cancellation = false, message = invitationComment)
                                Log.d("gift - send result: ", "$giftItem")
                                parsedProductDataArray.add(giftItem)
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
        val call = iRetrofit?.deleteReservation(id = id) ?:return

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