package com.shall_we.retrofit

import android.annotation.SuppressLint
import android.util.Log
import com.google.gson.JsonElement
import com.shall_we.dto.ExperienceExpCategoryRes
import com.shall_we.dto.ExperienceRes
import com.shall_we.dto.MainSttCategoryRes
import com.shall_we.dto.PopularRes
import com.shall_we.dto.ReservationItem
import com.shall_we.dto.SttCategoryData
import com.shall_we.dto.UpdateReservationReq
import com.shall_we.dto.UserDetail
import com.shall_we.dto.ValidTimeRes
import com.shall_we.dto.catergoryResponse
import com.shall_we.giftExperience.ReservationService
import com.shall_we.login.data.AuthResponse
import com.shall_we.login.data.AuthSignOutResponse
import com.shall_we.login.signup.UserData
import com.shall_we.login.data.AuthTokenData
import com.shall_we.login.data.UserInactiveReq
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.shall_we.myAlbum.MyAlbumData
import com.shall_we.myAlbum.PhotoInfo
import com.shall_we.mypage.MyGiftData
import okhttp3.MultipartBody
import java.text.SimpleDateFormat
import java.util.Locale

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
        completion: (RESPONSE_STATE, ArrayList<MainSttCategoryRes>?) -> Unit
    ) {
        val call =
            iRetrofit?.experienceGiftSttCategory(categoryId = categoryId, category = category)
                ?: return

        call.enqueue(object : Callback<SttCategoryData> {
            // 응답 성공
            override fun onResponse(call: Call<SttCategoryData>, response: Response<SttCategoryData>) {
                Log.d(
                    "retrofit",
                    "RetrofitManager - onResponse() called / response : ${response.code()}"
                )

                when (response.code()) {
                    200 -> {
                            completion(RESPONSE_STATE.OKAY, response.body()?.data)
                        }
                    else -> {
                        completion(RESPONSE_STATE.OKAY, null)

                    }
                    }

            }

            // 응답 실패
            override fun onFailure(call: Call<SttCategoryData>, t: Throwable) {
                Log.d("retrofit", "RetrofitManager - onFailure() called / t: $t")
                completion(RESPONSE_STATE.FAIL, null)
            }

        })
    }

    // 카테고리별 경험 조회 api
    fun experienceGiftExpCategory(
        categoryId: Int,
        category: String,
        completion: (RESPONSE_STATE, ArrayList<ExperienceExpCategoryRes>?) -> Unit
    ) {
        val call =
            iRetrofit?.experienceGiftExpCategory(categoryId = categoryId, category = category)
                ?: return

        call.enqueue(object : Callback<catergoryResponse> {
            // 응답 성공
            override fun onResponse(call: Call<catergoryResponse>, response: Response<catergoryResponse>) {
                Log.d(
                    "retrofit",
                    "RetrofitManager - onResponse() called / response : ${response.code()}"
                )

                when (response.code()) {
                    200 -> {
                            completion(RESPONSE_STATE.OKAY, response.body()?.data)
                        }
                    else -> {
                        completion(RESPONSE_STATE.OKAY, null)

                    }
                    }
                }


            // 응답 실패
            override fun onFailure(call: Call<catergoryResponse>, t: Throwable) {
                Log.d("retrofit", "RetrofitManager - onFailure() called / t: $t")
                completion(RESPONSE_STATE.FAIL, null)

            }

        })
    }

    fun experienceGiftSearch(
        title: String,
        completion: (RESPONSE_STATE, ArrayList<ExperienceRes>?) -> Unit
    ) {
        val call = iRetrofit?.experienceGiftSearch(title = title) ?: return

        call.enqueue(object : Callback<PopularRes> {
            // 응답 성공
            override fun onResponse(call: Call<PopularRes>, response: Response<PopularRes>) {
                Log.d(
                    "retrofit",
                    "RetrofitManager - onResponse() called / response : ${response.code()}"
                )

                when (response.code()) {
                    200 -> {
                            completion(RESPONSE_STATE.OKAY, response.body()?.data)
                        }
                    else -> {
                        completion(RESPONSE_STATE.OKAY, null)

                    }
                }
            }

            // 응답 실패
            override fun onFailure(call: Call<PopularRes>, t: Throwable) {
                Log.d("retrofit", "RetrofitManager - onFailure() called / t: $t")
                completion(RESPONSE_STATE.FAIL, null)

            }

        })
    }

    fun experienceGiftPopular(completion: (RESPONSE_STATE, ArrayList<ExperienceRes>?) -> Unit) {
        val call = iRetrofit?.experienceGiftPopular() ?: return

        call.enqueue(object : Callback<PopularRes> {
            // 응답 성공
            override fun onResponse(call: Call<PopularRes>, response: Response<PopularRes>) {
                Log.d(
                    "retrofit",
                    "RetrofitManager1 - onResponse() called / response : ${response.body()}"
                )

                when (response.code()) {
                    200 -> {
                        completion(RESPONSE_STATE.OKAY, response.body()?.data)

                    }
                }
            }

            // 응답 실패
            override fun onFailure(call: Call<PopularRes>, t: Throwable) {
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

                            Log.d("JSON Object", "${body}")

                            val gifts = body.getAsJsonArray("data")
                            gifts.forEach { resultItem ->
                                val resultItemObject = resultItem.asJsonObject
                                val reservationId : Int = resultItemObject.get("reservationId").asInt
                                val title: String = resultItemObject.get("experienceTitle").asString
                                val subtitle: String = resultItemObject.get("experienceSubTitle").asString

                                val dateTime : String = resultItemObject.get("date").asString
                                val invitationComment : String = resultItemObject.get("invitationComment").asString

                                val sdf = SimpleDateFormat("yyyy-MM-dd")
                                val dateTimeStr = sdf.parse(dateTime)

                                // Date 객체에서 날짜와 시간 추출
                                val dateFormatter = SimpleDateFormat("yyyy.MM.dd")
                                val date = dateFormatter.format(dateTimeStr)

//                                var time: String = ""
//                                val timeElement = resultItemObject?.get("time")
//                                val timeObj = if (timeElement != null && !timeElement.isJsonNull) {
//                                    JsonParser.parseString(timeElement.toString()).asJsonObject
//                                } else {
//                                    null
//                                }
                                val time: String = resultItemObject?.get("time")?.asString ?: ""

                                val receiver = resultItemObject.getAsJsonObject("receiver")
                                val name = receiver.get("name").asString
                                val giftItem = MyGiftData(idx = reservationId, title = title, description = subtitle, date = date, time = time, cancellable = false, message = invitationComment, person = name)
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

    fun usersGiftReceive(completion:(RESPONSE_STATE,ArrayList<MyGiftData>?,ArrayList<Int>?) -> Unit){
        val call = iRetrofit?.usersGiftReceive() ?:return
        call.enqueue(object : Callback<JsonElement> {
            // 응답 성공
            @SuppressLint("SimpleDateFormat")
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                Log.d(
                    "retrofit",
                    "RetrofitManager - onResponse() called / response : ${response.code()}"
                )
                when (response.code()) {
                    200 -> {
                        response.body()?.let {
                            var parsedProductDataArray = ArrayList<MyGiftData>()
                            var experienceGiftIdArray = ArrayList<Int>()
                            val body = it.asJsonObject

                            Log.d("JSON Object", "${body}")

                            val gifts = body.getAsJsonArray("data")

                            gifts.forEach { resultItem ->
                                val resultItemObject = resultItem.asJsonObject
                                val reservationId : Int = resultItemObject.get("reservationId").asInt
                                val title: String = resultItemObject.get("experienceTitle").asString
                                val subtitle: String = resultItemObject.get("experienceSubTitle").asString

                                val dateTime : String = resultItemObject.get("date").asString
                                val invitationComment : String = resultItemObject.get("invitationComment").asString

                                val sdf = SimpleDateFormat("yyyy-MM-dd")
                                val dateTimeStr = sdf.parse(dateTime)

                                // Date 객체에서 날짜와 시간 추출
                                val dateFormatter = SimpleDateFormat("yyyy.MM.dd")
                                val date = dateFormatter.format(dateTimeStr)

                                val timeElement: String = resultItemObject?.get("time")?.toString() ?: "99"
                                Log.d("timeElement","$timeElement")
                                val time: String = resultItemObject?.get("time")?.asString ?: ""
                            val sender = resultItemObject.getAsJsonObject("sender")
                                val name = sender.get("name").asString
                                val experienceGiftId : Int = resultItemObject.get("experienceGiftId").asInt


                                val giftItem = MyGiftData(idx = reservationId, title = title, description = subtitle, date = date, time = time, cancellable = true, message = invitationComment, person = name)
                                Log.d("gift - receive result: ", "$giftItem")
                                parsedProductDataArray.add(giftItem)
                                experienceGiftIdArray.add(experienceGiftId)

                            }
                            completion(RESPONSE_STATE.OKAY, parsedProductDataArray,experienceGiftIdArray)
                        }
                    }
                }
            }

            // 응답 실패
            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Log.d("retrofit", "RetrofitManager - onFailure() called / t: $t")
                completion(RESPONSE_STATE.FAIL, null, null)
            }

        })
    }

    fun tokenRefresh(refreshToken: RefreshTokenArray, completion: (RESPONSE_STATE, AuthTokenData?) -> Unit) {
        val call = iRetrofit?.tokenRefresh(refreshToken) ?: return
        call.enqueue(object : Callback<AuthResponse> {
            // 응답 성공인 경우
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                if(response.code() == 200){
                    val authResponse = response.body()
                    if (authResponse != null) {
                        Log.e("login", "Success: ${authResponse}")
                        completion(RESPONSE_STATE.OKAY, authResponse.data)
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

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
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

    fun uploadImg(image: MultipartBody.Part, url: String, endPoint:String, completion:(RESPONSE_STATE) -> Unit){
        val getRetrofit = RetrofitClient.getClient2(url)?.create(IRetrofit::class.java)
        val call = getRetrofit?.uploadImg(url =endPoint, image = image) ?:return


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
    fun getMemoryPhoto(date: String, completion:(RESPONSE_STATE, JsonElement?) -> Unit){
        val call = iRetrofit?.getMemoryPhoto(date = date) ?:return

        call.enqueue(object : Callback<JsonElement> {
            // 응답 성공
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                Log.d("retrofit","RetrofitManager - memoryPhoto onResponse() called / response : ${response.code()}")
                when (response.code()) {
                    200 -> {
                        Log.d("getMemoryPhoto response, date", "${response.body()}, $date")
                        response.body()?.let {
////                            var parsedMyAlbumDataArray = mutableListOf<PhotoInfo>()
//                            val body = it.asJsonObject
//                            val data = body.getAsJsonArray("data")
//                            data?.forEach { resultItem ->
//                                val resultItemObject = resultItem.asJsonObject
//                                val memoryPhotoImagesArray =
//                                    resultItemObject.getAsJsonArray("memoryPhotoImages")
//
//                                memoryPhotoImagesArray?.forEach { photoItem ->
//                                    val photoItemObject = photoItem.asJsonObject
//                                    val isUploader =
//                                        photoItemObject.get("isUploader").asBoolean
//                                    val memoryPhotoImgUrl =
//                                        photoItemObject.get("memoryPhotoImgUrl").asString
//
//                                    // Create MyAlbumPhotoData object and add to the list
//                                    val photoData =
//                                        PhotoInfo(isUploader, memoryPhotoImgUrl)
//                                    Log.d("photoData","$photoData")
//                                    parsedMyAlbumDataArray.add(photoData)
////
//                                val myAlbumItem = MyAlbumData(date = date, memoryImgs = memoryPhotoImages.toList())
//
//                                parsedMyAlbumDataArray.add(myAlbumItem)
////
////                                //val myalbum = it.asJsonObject
                            //    }
                            //parsedMyAlbumDataArray

                            //}
                            Log.d("getMemoryBody","${it}")
                            completion(RESPONSE_STATE.OKAY, response.body())

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

    fun deleteMemoryPhoto(photoUrl: String, completion: (RESPONSE_STATE, Any?) -> Unit){
        val call = iRetrofit?.deleteMemoryPhoto(photoUrl = photoUrl) ?:return

        call.enqueue(object : Callback<JsonElement> {
            // 응답 성공
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                when(response.code()){
                    200 -> {
//                        response.body()?.let{
//                            val body = it.asJsonObject
//
//                        }
                        completion(RESPONSE_STATE.OKAY, response.body())
                    }
                }
            }

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                completion(RESPONSE_STATE.FAIL, null)
            }

        })
    }

    fun postSignOut(refreshToken: RefreshTokenArray, completion: (RESPONSE_STATE, AuthSignOutResponse?) -> Unit) {
        val call = iRetrofit?.authSignOut(refreshToken) ?: return
        call.enqueue(object : Callback<AuthSignOutResponse> {
            // 응답 성공인 경우
            override fun onResponse(call: Call<AuthSignOutResponse>, response: Response<AuthSignOutResponse>) {
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

            override fun onFailure(call: Call<AuthSignOutResponse>, t: Throwable) {
            }
        })
    }
    fun getUserInfo(completion: (RESPONSE_STATE, String?) -> Unit){
        val call = iRetrofit?.getUserInfo()?.enqueue(object : Callback<UserDetail>{
            override fun onResponse(call: Call<UserDetail>, response: Response<UserDetail>) {
                val response1 = response.body()
                Log.d("retrofit",response1.toString())
                completion(RESPONSE_STATE.OKAY, response.body()?.data?.phoneNumber.toString())

            }

            override fun onFailure(call: Call<UserDetail>, t: Throwable) {
                completion(RESPONSE_STATE.OKAY, null)
            }

        })
    }

    fun userInactive(complain : UserInactiveReq, completion: (RESPONSE_STATE) -> Unit){
        val call = iRetrofit?.userInactive(complain)?.enqueue(object : Callback<JsonElement>{
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                val response = response.code()
                if(response == 200){
                    completion(RESPONSE_STATE.OKAY)
                }
                else{
                    completion(RESPONSE_STATE.FAIL)
                }
            }

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                completion(RESPONSE_STATE.FAIL)
            }
        })
    }


    fun get_reservation_date(
        ExperienceGiftId: Int, date:String,
        completion: (RESPONSE_STATE, List<ReservationItem>?) -> Unit
    ) {
        ReservationService.reservationService?.get_reservation_time(ExperienceGiftId,date)
            ?.enqueue(object : Callback<ValidTimeRes> {
                override fun onResponse(
                    call: Call<ValidTimeRes>,
                    response: Response<ValidTimeRes>
                ) {
                    if (response.isSuccessful ) {
                        completion(RESPONSE_STATE.OKAY, response.body()?.data)
                    }
                }

                override fun onFailure(call: Call<ValidTimeRes>, t: Throwable) {
                    Log.d(
                        "whatisthis",
                        "get_experience_detail_data, 네트워크 오류가 발생했습니다. " + t.message.toString()
                    )
                    completion(RESPONSE_STATE.FAIL, null)
                }
            })
    }


}