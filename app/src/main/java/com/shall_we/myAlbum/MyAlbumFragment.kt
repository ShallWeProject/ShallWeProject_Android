package com.shall_we.myAlbum

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState
import com.amazonaws.regions.Regions
import com.google.android.material.tabs.TabLayout
import com.shall_we.BuildConfig.access_key
import com.shall_we.BuildConfig.secret_key
import com.shall_we.R
import com.shall_we.databinding.FragmentMyAlbumBinding
import com.shall_we.mypage.MyGiftData
import com.shall_we.retrofit.BodyData
import com.shall_we.retrofit.RESPONSE_STATE
import com.shall_we.retrofit.RetrofitManager
import com.shall_we.retrofit.UploadPhotoArray
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class MyAlbumFragment : Fragment() ,MyAlbumAdapter.OnItemClickListener {
    private lateinit var binding: FragmentMyAlbumBinding
    private lateinit var adapter: MyAlbumAdapter

    private lateinit var albumData: MyAlbumPhotoData
    private lateinit var giftData: ArrayList<MyGiftData>
    private var giftIdx = 0
    private lateinit var dates: List<DateTime>
    data class DateTime(val date: String, val time: String)
    private val firstAdd: PhotoInfo = PhotoInfo(isUploader = false, memoryPhotoImgUrl = "R.drawable.add_image")

    private var selectedImageUri: Uri = Uri.EMPTY
    private var presignedUrl: String = ""
    private var imageKey: String = ""
    private var filename: String = ""
    private var ext: String = ""
    private lateinit var file: File
    private var path: Uri = Uri.EMPTY

    private lateinit var requestBody: RequestBody


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 사진첩에 들어오면 권한 요청함.
        requestPermission()
        // 사진첩 추억의 날짜 리스트를 받아옴.
        retrofitCallDate()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyAlbumBinding.inflate(inflater, container, false)

        // 왼쪽 화살표 클릭시 다음 인덱스로 이동 (더 오래된 날짜)
        binding.ivLeft.setOnClickListener {
            if (giftData.size > giftIdx + 1) {
                giftIdx += 1
                binding.tvAlbumDate.text = dates[giftIdx].date
                binding.tvAlbumDescription.text = giftData[giftIdx].description
                binding.tvAlbumTitle.text = giftData[giftIdx].title
                val modDate: String = giftData[giftIdx].date.replace(".", "-")
                getMemoryPhoto(modDate, giftData[giftIdx].time)
                Log.d("album date", "album date ++ is not null")
            } else {
                Log.d("album date", "album date ++ is null, ${binding.tvAlbumDate.text}")
            }
        }

        // 오른쪽 화살표 클릭시 이전 인덱스로 이동 (더 최신의 날짜)
        binding.ivRight.setOnClickListener {
            if (giftIdx >= 1) {
                giftIdx -= 1
                binding.tvAlbumDate.text = dates[giftIdx].date
                binding.tvAlbumDescription.text = giftData[giftIdx].description
                binding.tvAlbumTitle.text = giftData[giftIdx].title
                val modDate: String = giftData[giftIdx].date.replace(".", "-")
                getMemoryPhoto(modDate, giftData[giftIdx].time)
                Log.d("album date", "album date -- is not null")
            } else {
                Log.d("album date", "album date -- is null, ${binding.tvAlbumDate.text}")
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // RecyclerView에 GridLayoutManager 적용
        val gridLayoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerAlbumView.layoutManager = gridLayoutManager

    }

    // 버킷 내의 이미지를 해당 추억에 post
    private fun postMemoryPhoto(uploadPhotoArray: UploadPhotoArray) {
        Log.d("postmemoryphoto",uploadPhotoArray.toString())
        RetrofitManager.instance.postMemoryPhoto(
            uploadPhotoArray = uploadPhotoArray,
            completion = { responseState ->
                when (responseState) {
                    RESPONSE_STATE.OKAY -> {
                        val modDate: String = giftData[giftIdx].date.replace(".", "-")
                        getMemoryPhoto(modDate, giftData[giftIdx].time)
                        Log.d("retrofit", "post Memory Photo api : ${responseState}")
                    }

                    RESPONSE_STATE.FAIL -> {
                        Log.d("retrofit", "api 호출 에러")
                    }
                }
            })
    }

    private fun initAlbum(resultData: MyAlbumPhotoData) {
        adapter = MyAlbumAdapter(requireContext())
        binding.recyclerAlbumView.adapter = adapter
        adapter.setOnItemClickListener(this)

        Log.d("retrofit", "initAlbum, $resultData")

        adapter.datas = resultData
        adapter.notifyDataSetChanged()
    }

    var size: Int = 0
    // user gift receive 를 통해 예약 정보를 받아옴
    private fun retrofitCallDate() {
        val giftBoxData: ArrayList<MyGiftData> = arrayListOf()

        RetrofitManager.instance.usersGiftSend(
            completion = { responseState, responseBody ->
                when (responseState) {
                    RESPONSE_STATE.OKAY -> {
                        Log.d("retrofit", "mygift api : ${responseBody?.size}")
                        if (responseBody?.size != 0) {
                            giftBoxData.addAll(responseBody!!)
                        }
                        size = responseBody.size
                        retrofitCallDate2(giftBoxData)
                    }

                    RESPONSE_STATE.FAIL -> {
                        Log.d("retrofit", "api 호출 에러")
                    }
                }
            }
        )
    }

    private fun retrofitCallDate2(giftBoxData: ArrayList<MyGiftData>){
        RetrofitManager.instance.usersGiftReceive(
            completion = { responseState, responseBody, expId ->
                when (responseState) {
                    RESPONSE_STATE.OKAY -> {
                        Log.d("retrofit", "mygift api : ${responseBody?.size}")
                        if (responseBody?.size == 0 && size == 0) {
                            emptyOrNot(true)
                        } else {
                            emptyOrNot(false)
                            giftBoxData.addAll(responseBody!!)
                            getGiftData(giftBoxData)
                        }
                    }

                    RESPONSE_STATE.FAIL -> {
                        Log.d("retrofit", "api 호출 에러")
                    }
                }
            }
        )
    }

    private fun emptyOrNot(boolean: Boolean) {
        if (boolean) {
            binding.tvNoData.visibility = View.VISIBLE
            binding.cvAlbum.visibility = View.GONE
        }
        else {
            binding.tvNoData.visibility = View.GONE
            binding.cvAlbum.visibility = View.VISIBLE
        }
    }

    private fun extractHour(time: String): String {
        val sdf1 = SimpleDateFormat("HH시")
        val sdf2 = SimpleDateFormat("HH:mm:ss")
        val date1: Date? = try { sdf1.parse(time) } catch (e: Exception) { null }
        val date2: Date? = try { sdf2.parse(time) } catch (e: Exception) { null }

        return when {
            date1 != null -> SimpleDateFormat("HH").format(date1)
            date2 != null -> SimpleDateFormat("HH").format(date2)
            else -> ""
        }
    }

    // 위의 retrofitCallDate에서 호출하는 함수. get memory-photo 결과 리스트 재정렬해서 giftData, dates에 저장
    private fun getGiftData(resultData: ArrayList<MyGiftData>): List<DateTime> {
        // gift/receive 의 결과 리스트를 받아옴.
        giftData = ArrayList(resultData) //ArrayList(resultData)
        // 날짜 최신순으로 정렬.
        giftData = giftData.sortedWith(compareByDescending<MyGiftData> { it.date.replace(".", "").toInt() }
            .thenByDescending { extractHour(it.time).toInt() }
        ).toMutableList() as ArrayList<MyGiftData>
        Log.d("retrofit", "original data: $resultData")
        Log.d("retrofit", "replaced data: $giftData")
        dates =
            giftData.map { DateTime(it.date, it.time) } // 이게 굳이 있어야 하나 싶긴 함. giftData로 받아와서 title이랑 date 다 출력할 수 있지 않나 싶음...
        binding.tvAlbumDate.text = dates[giftIdx].date
        binding.tvAlbumDescription.text = giftData[giftIdx].description
        binding.tvAlbumTitle.text = giftData[giftIdx].title
        val modDate: String = giftData[giftIdx].date.replace(".", "-")
        getMemoryPhoto(modDate, giftData[giftIdx].time)
        Log.d("modDate","$modDate")
        return dates
    }

    @SuppressLint("SuspiciousIndentation")
    private fun getMemoryPhoto(date: String, time: String) {
        RetrofitManager.instance.getMemoryPhoto(
            date = date
        ) { responseState, responseBody ->
            when (responseState) {
                RESPONSE_STATE.OKAY -> {
//                    Log.d("getMemoryPhoto Okay", "myalbum api : ${responseBody?.size}")
                    Log.d("responseBody", "$responseBody")
//
                    albumData = MyAlbumPhotoData(myPhoto = mutableListOf(firstAdd))
                    if (responseBody != null) {
//                        albumData.myPhoto.addAll(responseBody)
//                    }
                        val body = responseBody.asJsonObject
                        val data = body?.getAsJsonArray("data")
                        data?.forEach { resultItem ->
                            val resultItemObject = resultItem.asJsonObject
                            Log.d("data resultItemObject", "$resultItemObject")
                            if (resultItemObject.get("time").asString == time) {
                                val memoryPhotoImagesArray =
                                    resultItemObject.getAsJsonArray("memoryPhotoImages")

                                memoryPhotoImagesArray?.forEach { photoItem ->
                                    val photoItemObject = photoItem.asJsonObject
                                    val isUploader =
                                        photoItemObject.get("isUploader").asBoolean
                                    val memoryPhotoImgUrl =
                                        photoItemObject.get("memoryPhotoImgUrl").asString

                                    // Create MyAlbumPhotoData object and add to the list
                                    val photoData =
                                        PhotoInfo(isUploader, memoryPhotoImgUrl)
                                    Log.d("photoData", "$photoData")
                                    albumData.myPhoto.add(photoData)

                                    Log.d("get Memory Photo", "${albumData}")
                                }
                            }
                        }
                        initAlbum(albumData)
                    }
                    Log.d("responseBody", "$responseBody")

                }

                RESPONSE_STATE.FAIL -> {
                    Log.d("retrofit", "api 호출 에러")
                }
            }
        }
    }


    // 사진 접근 권한 요청
    private fun requestPermission() {
        val locationResultLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) {
            if (!it) {
                Toast.makeText(view?.context, "스토리지에 접근 권한을 허가해주세요", Toast.LENGTH_SHORT).show()
            }
        }
        locationResultLauncher.launch(
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        )
    }

    private val PICK_IMAGE_REQUEST = 1 // 요청 코드

    // 갤러리 열기
    private fun openGallery() {
        Log.d("gallery", "갤러리")
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    // startActivityForResult로부터 결과 처리
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            // 선택한 이미지의 Uri 가져오기
            selectedImageUri = data?.data!!
            path = selectedImageUri
            val fileUri =
                selectedImageUri?.let {
                    getImageAbsolutePath(
                        it,
                        requireContext()
                    )?.toUri()
                }!! // 선택한 이미지의 경로를 구하는 함수 호출
            if (fileUri != null) {
                this.selectedImageUri = fileUri
                parseUri(fileUri)
                file = File(fileUri.toString())
                CoroutineScope(Dispatchers.IO).launch {
                    getImgUrl(ext, "uploads", filename, file)
                    uploadImg(requestBody)
                }
            }
        }
    }

    private fun parseUri(selectedImageUri: Uri) {
        val fileForName = File(selectedImageUri.toString())
        val currentTime : Long = System.currentTimeMillis() // ms로 반환
        println(currentTime)
        val dateFormat = SimpleDateFormat("HHmmss")

        val formattedTime: String = dateFormat.format(Date(currentTime))

        // Print the formatted time
        println(formattedTime)
        filename = fileForName.nameWithoutExtension+formattedTime
        ext = fileForName.extension
//        Log.d("fileForName", "$fileForName")
//        Log.d("filename, ext", "$filename $ext")
//        Log.d("Album Result", "$selectedImageUri")

    }

    private suspend fun getImgUrl(ext: String, dir: String, filename: String, file: File) {
        val data = BodyData(ext = ext, dir = dir, filename = filename)

        Log.d("data", "$data")

        try {
            val response = suspendCoroutine { continuation ->
                RetrofitManager.instance.getImgUrl(data) { responseState, imageKey, presignedUrl ->
                    continuation.resume(Triple(responseState, imageKey, presignedUrl))
                }
            }

            when (response.first) {
                RESPONSE_STATE.OKAY -> {
                    Log.d("retrofit", "getImgUrl api : ${response.first}")
                    imageKey = response.second.substringAfter("\"").removeSuffix("\"")
                    presignedUrl = response.third.substringAfter("https://shallwebucket.s3.ap-northeast-2.amazonaws.com/").removeSuffix("\"")

                    Log.d("imageKey", "$imageKey")
                    // 파일의 MIME 유형 가져오기 (예: text/plain, image/jpeg 등)
                    val mediaType = "image/*".toMediaTypeOrNull() // 파일의 MIME 유형에 따라 변경

                    // RequestBody로 파일을 래핑
                    requestBody = file.asRequestBody(mediaType)
                }

                RESPONSE_STATE.FAIL -> {
                    Log.d("retrofit", "api 호출 에러")
                }
            }
        } catch (e: Exception) {
            Log.e("retrofit", "getImgUrl 호출 에러", e)
        }


}


    override fun onItemClick(position: Int) {
        // 클릭된 아이템이 첫 번째 아이템(사진 추가 버튼)일 때
        if (position == 0)
            openGallery()
    }

    // Uri에서 절대 경로 추출하기
    private fun getImageAbsolutePath(uri: Uri, context: Context): String? {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        var cursor: Cursor? = null
        var path: String? = null
        try {
            cursor = context.contentResolver.query(uri, projection, null, null, null)
            cursor?.let {
                if (it.moveToFirst()) {
                    val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                    path = it.getString(columnIndex)
                    Log.d("getImageAbsolutePath", "절대경로 추출 uri = $path")
                }
            }
        } catch (e: Exception) {
            Log.e("getImageAbsolutePath", "절대경로 추출 오류: ${e.message}")
        } finally {
            cursor?.close()
        }
        return path
    }

    private suspend fun uploadImg(image: RequestBody) {
        try {
            val response = suspendCoroutine{ continuation ->
                RetrofitManager.instance.uploadImg(
                    image = image,
                    url ="https://shallwebucket.s3.ap-northeast-2.amazonaws.com/",
                    endPoint = presignedUrl
                ) { responseState, responseCode ->
                    continuation.resume(Pair(responseState, responseCode))
                }
            }

            when (response.first) {
                RESPONSE_STATE.OKAY -> {
                    Log.d("retrofit", "uploadImg api : ${response.first}")
                }

                RESPONSE_STATE.FAIL -> {
                    Log.d("retrofit", "uploadImg api 호출 에러")
                }
            }

            val uploadPhotoArray = UploadPhotoArray(imageKey, giftData[giftIdx].idx)
            Log.d("post Memory Photo", "$uploadPhotoArray")
            postMemoryPhoto(uploadPhotoArray)
        } catch (e: Exception) {
            Log.e("retrofit", "uploadImg 호출 에러", e)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        val tab = requireActivity().findViewById<TabLayout>(R.id.tabs)
        tab.visibility = View.VISIBLE
        val fab = requireActivity().findViewById<Button>(R.id.fab_album)
        fab.visibility = View.VISIBLE
    }
}

