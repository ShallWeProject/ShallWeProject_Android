package com.shall_we.retrofit

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.shall_we.dto.UpdateReservationReq
import com.shall_we.home.ProductData
import com.shall_we.signup.UserData
import com.shall_we.login.data.Auth
import com.shall_we.login.data.AuthLogin
import com.shall_we.login.data.AuthResponse
import com.shall_we.login.data.AuthTokenData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.shall_we.myAlbum.MyAlbumData
import com.shall_we.mypage.MyGiftData
import java.text.SimpleDateFormat

class RetrofitManager {
    companion object {
        val instance = RetrofitManager()
    }

    // http 콜 만들기
    // 레트로핏 인터페이스 가져오기
    private val iRetrofit: IRetrofit? =
        RetrofitClient.getClient(API.BASE_URL)?.create(IRetrofit::class.java)
    private val Retrofit: IRetrofit? =
        RetrofitClient.getClient2("https://668h6987ib.execute-api.ap-northeast-2.amazonaws.com")?.create(IRetrofit::class.java)


    // 상황별 추천경험 조회 api
    fun experienceGiftSttCategory(
        categoryId: Int,
        category: String,
        completion: (RESPONSE_STATE, ArrayList<ProductData>?) -> Unit
    ) {
        val call =
            iRetrofit?.experienceGiftSttCategory(categoryId = categoryId, category = category)
                ?: return

        call.enqueue(object : Callback<JsonElement> {
            // 응답 성공
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                Log.d(
                    "retrofit",
                    "RetrofitManager - onResponse() called / response : ${response.code()}"
                )

                when (response.code()) {
                    200 -> {
                        response.body()?.let {
                            var parsedProductDataArray = ArrayList<ProductData>()
                            val body = it.asJsonObject
                            val data = body.getAsJsonArray("data")

                            data.forEach { resultItem ->
                                val resultItemObject = resultItem.asJsonObject
                                val title: String = resultItemObject.get("title").asString
                                val subtitle: String = resultItemObject.get("subtitleTitle").asString
                                val price: String = resultItemObject.get("price").asString
                                val formattedPrice = String.format("%,d", price.toInt())

                                val img: String = resultItemObject.get("giftImgUrl").asString
                                val giftid: Int = resultItemObject.get("experienceGiftId").asInt

                                val productItem = ProductData(
                                    title = title,
                                    subtitle = subtitle,
                                    price = formattedPrice,
                                    img = img,
                                    giftid = giftid
                                )

                                parsedProductDataArray.add(productItem)
                            }
                            completion(RESPONSE_STATE.OKAY, parsedProductDataArray)
                        }
                    }
                }
            }

            // 응답 실패
            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Log.d("retrofit", "RetrofitManager - onFailure() called / t: $t")
                completion(RESPONSE_STATE.FAIL, null)
            }

        })
    }

    // 카테고리별 경험 조회 api
    fun experienceGiftExpCategory(
        categoryId: Int,
        category: String,
        completion: (RESPONSE_STATE, ArrayList<ProductData>?) -> Unit
    ) {
        val call =
            iRetrofit?.experienceGiftExpCategory(categoryId = categoryId, category = category)
                ?: return

        call.enqueue(object : Callback<JsonElement> {
            // 응답 성공
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                Log.d(
                    "retrofit",
                    "RetrofitManager - onResponse() called / response : ${response.code()}"
                )

                when (response.code()) {
                    200 -> {
                        response.body()?.let {
                            var parsedProductDataArray = ArrayList<ProductData>()
                            val body = it.asJsonObject
                            val data = body.getAsJsonArray("data")

                            data.forEach { resultItem ->
                                val resultItemObject = resultItem.asJsonObject
                                val title: String = resultItemObject.get("title").asString
                                val subtitle: String =
                                    resultItemObject.get("subtitleTitle").asString
                                val price: String = resultItemObject.get("price").asString
                                val formattedPrice = String.format("%,d", price.toInt())

                                val img: String = resultItemObject.get("giftImgUrl").asString
                                val giftid: Int = resultItemObject.get("experienceGiftId").asInt

                                val productItem = ProductData(
                                    title = title,
                                    subtitle = subtitle,
                                    price = formattedPrice,
                                    img = img,
                                    giftid = giftid
                                )
                                parsedProductDataArray.add(productItem)
                            }
                            completion(RESPONSE_STATE.OKAY, parsedProductDataArray)
                        }
                    }
                }
            }

            // 응답 실패
            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Log.d("retrofit", "RetrofitManager - onFailure() called / t: $t")
                completion(RESPONSE_STATE.FAIL, null)

            }

        })
    }

    fun experienceGiftSearch(
        title: String,
        completion: (RESPONSE_STATE, ArrayList<ProductData>?) -> Unit
    ) {
        val call = iRetrofit?.experienceGiftSearch(title = title) ?: return

        call.enqueue(object : Callback<JsonElement> {
            // 응답 성공
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                Log.d(
                    "retrofit",
                    "RetrofitManager - onResponse() called / response : ${response.code()}"
                )

                when (response.code()) {
                    200 -> {
                        response.body()?.let {
                            var parsedProductDataArray = ArrayList<ProductData>()
                            val body = it.asJsonObject
                            val data = body.getAsJsonArray("data")

                            data.forEach { resultItem ->
                                val resultItemObject = resultItem.asJsonObject
                                val title: String = resultItemObject.get("title").asString
                                val subtitle: String = resultItemObject.get("subtitle").asString
                                val price: String = resultItemObject.get("price").asString
                                val formattedPrice = String.format("%,d", price.toInt())

                                val img: String = resultItemObject.get("giftImgUrl").asString
                                val giftid: Int = resultItemObject.get("experienceGiftId").asInt

                                val productItem = ProductData(
                                    title = title,
                                    subtitle = subtitle,
                                    price = formattedPrice,
                                    img = img,
                                    giftid = giftid
                                )

                                parsedProductDataArray.add(productItem)
                            }
                            completion(RESPONSE_STATE.OKAY, parsedProductDataArray)
                        }
                    }
                }
            }

            // 응답 실패
            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Log.d("retrofit", "RetrofitManager - onFailure() called / t: $t")
                completion(RESPONSE_STATE.FAIL, null)

            }

        })
    }

    fun experienceGiftPopular(completion: (RESPONSE_STATE, ArrayList<ProductData>?) -> Unit) {
        val call = iRetrofit?.experienceGiftPopular() ?: return

        call.enqueue(object : Callback<JsonElement> {
            // 응답 성공
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                Log.d(
                    "retrofit",
                    "RetrofitManager1 - onResponse() called / response : ${response.code()}"
                )

                when (response.code()) {
                    200 -> {
                        response.body()?.let {
                            var parsedProductDataArray = ArrayList<ProductData>()
                            val body = it.asJsonObject
                            val data = body.getAsJsonArray("data")

                            data.forEach { resultItem ->
                                val resultItemObject = resultItem.asJsonObject
                                val title: String = resultItemObject.get("title").asString
                                val subtitle: String = resultItemObject.get("subtitle").asString
                                val price: String = resultItemObject.get("price").asString
                                val formattedPrice = String.format("%,d", price.toInt())
                                val img: String = resultItemObject.get("giftImgUrl").asString
                                val giftid: Int = resultItemObject.get("experienceGiftId").asInt

                                val productItem = ProductData(
                                    title = title,
                                    subtitle = subtitle,
                                    price = formattedPrice,
                                    img = img,
                                    giftid = giftid
                                )
                                parsedProductDataArray.add(productItem)
                            }
                            completion(RESPONSE_STATE.OKAY, parsedProductDataArray)
                        }
                    }
                }
            }

            // 응답 실패
            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Log.d("retrofit", "RetrofitManager - onFailure() called / t: $t")
                completion(RESPONSE_STATE.FAIL, null)

            }

        })
    }

    fun sendOne(phoneNumber: SendOneArray, completion: (RESPONSE_STATE, JsonElement?) -> Unit) {
        val call = iRetrofit?.sendOne(phoneNumber) ?: return
        call.enqueue(object : Callback<JsonElement> {
            // 응답 성공인 경우
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                Log.d(
                    "retrofit",
                    "RetrofitManager - onResponse() called / response : ${response.message()}"
                )

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

    fun usersPatch(userData: UserData, completion: (RESPONSE_STATE, Int) -> Unit) {
        val call = iRetrofit?.usersPatch(userData) ?: return

        call.enqueue(object : Callback<JsonElement> {
            // 응답 성공
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                Log.d(
                    "retrofit",
                    "RetrofitManager - onResponse() called / response : ${response.body()}"
                )
                val body = response.body()?.asJsonObject
                val data = body?.getAsJsonObject("message")?.asString
                Log.d(
                    "retrofit",
                    "RetrofitManager - onResponse() called / response : ${data}"
                )
                completion(RESPONSE_STATE.OKAY, response.code())
            }


            // 응답 실패
            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Log.d("retrofit", "RetrofitManager - onFailure() called / t: $t")
                completion(RESPONSE_STATE.FAIL, 0)

            }
        })
    }


    fun validVerification(
        validVerificationArray: ValidVerificationArray,
        completion: (RESPONSE_STATE, Int?) -> Unit
    ) {
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


    fun getMemoryPhoto(date: String, completion:(RESPONSE_STATE, ArrayList<MyAlbumData>?) -> Unit){
        val call = iRetrofit?.getMemoryPhoto(date=date) ?:return

        call.enqueue(object : Callback<JsonElement> {
            // 응답 성공
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
             Log.d("retrofit","RetrofitManager - memoryPhoto onResponse() called / response : ${response.code()}")
                when (response.code()) {
                    200 -> {
                        response.body()?.let {
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

                                val myAlbumItem = MyAlbumData(idx = reservationId, date = date, memoryImgs = memoryPhotoImages.toTypedArray())

                                parsedMyAlbumDataArray.add(myAlbumItem)
                            }

                            completion(RESPONSE_STATE.OKAY,parsedMyAlbumDataArray)
                        }
                    }
                }
            }

            // 응답 실패
            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Log.d("retrofit", "RetrofitManager - onFailure() called / t: $t")
                completion(RESPONSE_STATE.FAIL, null)
            }

        })
    }

    fun usersGiftSend(completion: (RESPONSE_STATE, ArrayList<MyGiftData>?) -> Unit) {
        val call = iRetrofit?.usersGiftSend() ?: return

        call.enqueue(object : Callback<JsonElement> {
            // 응답 성공
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                Log.d("retrofit","RetrofitManager - userGiftSend onResponse() called / response : ${response.code()}")
                when (response.code()) {
                    200 -> {
                        response.body()?.let {
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

                                val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                                val dateTimeStr = sdf.parse(dateTime)

                                // Date 객체에서 날짜와 시간 추출
                                val dateFormatter = SimpleDateFormat("yyyy.MM.dd")
                                val timeFormatter = SimpleDateFormat("HH시")
                                val date = dateFormatter.format(dateTimeStr)
                                val time = timeFormatter.format(dateTimeStr)

                                val giftItem = MyGiftData(idx = reservationId, title = title, description = subtitle, date = date, time = time, cancellable = false, message = invitationComment)
                                Log.d("gift - send result: ", "$giftItem")
                                parsedProductDataArray.add(giftItem)
                            }
                            completion(RESPONSE_STATE.OKAY, parsedProductDataArray)
                        }
                    }
                }
            }

            // 응답 실패
            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Log.d("retrofit", "RetrofitManager - onFailure() called / t: $t")
                completion(RESPONSE_STATE.FAIL, null)
            }

        })
    }

    fun usersGiftReceive(completion:(RESPONSE_STATE,ArrayList<MyGiftData>?) -> Unit){
        val call = iRetrofit?.usersGiftReceive() ?:return
        call.enqueue(object : Callback<JsonElement> {
            // 응답 성공
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                Log.d(
                    "retrofit",
                    "RetrofitManager - onResponse() called / response : ${response.code()}"
                )
                when (response.code()) {
                    200 -> {
                        response.body()?.let {
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

                                val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                                val dateTimeStr = sdf.parse(dateTime)

                                // Date 객체에서 날짜와 시간 추출
                                val dateFormatter = SimpleDateFormat("yyyy.MM.dd")
                                val timeFormatter = SimpleDateFormat("HH시")

                                val date = dateFormatter.format(dateTimeStr)
                                val time = timeFormatter.format(dateTimeStr)

                                val giftItem = MyGiftData(idx = reservationId, title = title, description = subtitle, date = date, time = time, cancellable = true, message = invitationComment)
                                Log.d("gift - send result: ", "$giftItem")
                                parsedProductDataArray.add(giftItem)
                            }
                            completion(RESPONSE_STATE.OKAY, parsedProductDataArray)
                        }
                    }
                }
            }

            // 응답 실패
            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Log.d("retrofit", "RetrofitManager - onFailure() called / t: $t")
                completion(RESPONSE_STATE.FAIL, null)
            }

        })
    }

    fun tokenRefresh(refreshToken: RefreshTokenArray, completion: (RESPONSE_STATE, AuthTokenData?) -> Unit) {
        val call = iRetrofit?.tokenRefresh(refreshToken) ?: return
        call.enqueue(object : Callback<AuthTokenData> {
            // 응답 성공인 경우
            override fun onResponse(call: Call<AuthTokenData>, response: Response<AuthTokenData>) {
                if(response.code() == 200){
                    val authResponse = response.body()
                    if (authResponse != null) {
                        Log.e("login", "Success: ${authResponse}")
                        completion(RESPONSE_STATE.OKAY, authResponse)
                    } else {
                        completion(RESPONSE_STATE.OKAY, null)

                    }
                }else{
                    try{
                        Log.e("login", "Request failed with response code: ${response.code()}")

                        completion(RESPONSE_STATE.OKAY, null)

                    }catch(e:Exception){
                        completion(RESPONSE_STATE.OKAY, null)

                    }
                }
            }

            override fun onFailure(call: Call<AuthTokenData>, t: Throwable) {
            }
        })
    }
     fun deleteReservation(id : Int, completion:(RESPONSE_STATE) -> Unit){
         val call = iRetrofit?.deleteReservation(id = id) ?:return

         call.enqueue(object : retrofit2.Callback<JsonElement>{
             // 응답 성공
             override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                 Log.d("retrofit","RetrofitManager - onResponse() called / response : ${response.code()}")

                 when(response.code()){
                     200 -> {
                         response.body()?.let{
                             val body = it.asJsonObject

                             }
                             completion(RESPONSE_STATE.OKAY)
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


    fun putUpdateReservation(updateReservationReq: UpdateReservationReq, completion:(RESPONSE_STATE) -> Unit){
        val call = iRetrofit?.putUpdateReservation(updateReservationReq = updateReservationReq) ?:return

        call.enqueue(object : retrofit2.Callback<JsonElement>{
            // 응답 성공
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                Log.d("retrofit","RetrofitManager - onResponse() called / response : ${response.code()}")

                when(response.code()){
                    200 -> {
                        response.body()?.let{
                            val body = it.asJsonObject
                        }
                        completion(RESPONSE_STATE.OKAY)
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

    fun getImgUrl(data: BodyData, completion:(RESPONSE_STATE, imageKey: String, presignedUrl: String) -> Unit){
//        val getRetrofit = RetrofitClient.getClient("https://668h6987ib.execute-api.ap-northeast-2.amazonaws.com")?.create(IRetrofit::class.java)
        val call = Retrofit?.getImgUrl(data) ?:return

        call.enqueue(object : retrofit2.Callback<JsonElement>{
            // 응답 성공
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                Log.d("retrofit","RetrofitManager - onResponse() called / response : ${response.code()}")

                when(response.code()){
                    200 -> {
                        response.body()?.let{
                            val body = it.asJsonObject
                            val imageKey = body.get("imageKey").toString()
                            val presignedUrl = body.get("presignedUrl").toString()
                            completion(RESPONSE_STATE.OKAY, imageKey, presignedUrl)
                            Log.d("retrofit","RetrofitManager - getImgUrl onResponse() called / response : ${response.code()}")
                        }
                    }
                }
            }

            // 응답 실패
            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Log.d("retrofit","RetrofitManager - onFailure() called / t: $t")
                completion(RESPONSE_STATE.FAIL, "null", "null")

            }

        })
    }

    fun uploadImg(imageBytes: ByteArray, url: String,endPoint:String, completion:(RESPONSE_STATE) -> Unit){
        val getRetrofit = RetrofitClient.getClient2(url)?.create(IRetrofit::class.java)
        val call = getRetrofit?.uploadImg(url=endPoint, imageBytes = imageBytes) ?:return


        call.enqueue(object : retrofit2.Callback<JsonElement>{
            // 응답 성공
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                Log.d("retrofit","RetrofitManager - onResponse() called / response : ${response}")

                when(response.code()){
                    200 -> {
                        completion(RESPONSE_STATE.OKAY)
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

    fun postMemoryPhoto(uploadPhotoArray: UploadPhotoArray, completion:(RESPONSE_STATE) -> Unit){
        val call = iRetrofit?.postMemoryPhoto(uploadPhotoArray) ?:return

        call.enqueue(object : retrofit2.Callback<JsonElement>{
            // 응답 성공
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                Log.d("retrofit","RetrofitManager - postMemoryPhoto onResponse() called / response : ${response.code()}")

                when(response.code()){
                    200 -> {
                        response.body()?.let{
                            val body = it.asJsonObject
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