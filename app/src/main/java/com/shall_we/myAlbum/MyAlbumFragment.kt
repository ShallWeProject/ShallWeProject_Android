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
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState
import com.amazonaws.regions.Regions
import com.shall_we.BuildConfig.access_key
import com.shall_we.BuildConfig.secret_key
import com.shall_we.databinding.FragmentMyAlbumBinding
import com.shall_we.mypage.MyGiftData
import com.shall_we.retrofit.RESPONSE_STATE
import com.shall_we.retrofit.RetrofitManager
import com.shall_we.retrofit.UploadPhotoArray
import com.shall_we.utils.S3Util
import java.io.File

class MyAlbumFragment : Fragment() ,MyAlbumAdapter.OnItemClickListener {
    private lateinit var binding: FragmentMyAlbumBinding
    private lateinit var adapter: MyAlbumAdapter

    private val albumData = ArrayList<MyAlbumPhotoData>()
    private val allPhotoData: ArrayList<MyAlbumPhotoData> = ArrayList()
    private lateinit var giftData: ArrayList<MyGiftData>
    private var giftIdx = 0
    private lateinit var dates: List<String>

    private var selectedImageUri: Uri = Uri.EMPTY
    private var filename: String = ""
    private var ext: String = ""
    private lateinit var file: File
    private var path: Uri = Uri.EMPTY


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermission()
        retrofitCallDate()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyAlbumBinding.inflate(inflater, container, false)

        // 왼쪽 화살표 클릭시 다음 인덱스로 이동 (더 오래된 날짜)
        binding.ivLeft.setOnClickListener {
            if (giftData.size > giftIdx + 1) {
                giftIdx += 1
                binding.tvAlbumDate.text = dates[giftIdx]
                binding.tvAlbumDescription.text = giftData[giftIdx].description
                binding.tvAlbumTitle.text = giftData[giftIdx].title
                val modDate: String = giftData[giftIdx].date.replace(".", "-")
                getMemoryPhoto(modDate)
                Log.d("album date", "album date ++ is not null")
            } else {
                Log.d("album date", "album date ++ is null, ${binding.tvAlbumDate.text}")
            }
        }

        // 오른쪽 화살표 클릭시 이전 인덱스로 이동 (더 최신의 날짜)
        binding.ivRight.setOnClickListener {
            if (giftIdx >= 1) {
                giftIdx -= 1
                binding.tvAlbumDate.text = dates[giftIdx]
                binding.tvAlbumDescription.text = giftData[giftIdx].description
                binding.tvAlbumTitle.text = giftData[giftIdx].title
                val modDate: String = giftData[giftIdx].date.replace(".", "-")
                getMemoryPhoto(modDate)
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

//        // Adapter 설정
//        albumadapter = MyAlbumAdapter(requireContext())
//        viewBinding.recyclerAlbumView.adapter = albumadapter
        // adapter.datas = // 데이터 리스트 설정
//        adapter = MyAlbumAdapter(requireContext())
//        viewBinding.recyclerAlbumView.adapter = adapter
//         adapter.datas = giftData[]// 데이터 리스트 설정

//        albumData.apply {
//            add(
//                MyAlbumData(
//                idx = 1, date = "2023-08-21", memoryImgs = arrayOf("img1.jpg", "img2.jpg")
//                )
//            )
//        }
        initAlbum(allPhotoData)
    }

    @RequiresApi(Build.VERSION_CODES.O)

    private fun upload() {
        S3Util.instance
            .setKeys(access_key, secret_key)
            .setRegion(Regions.AP_NORTHEAST_2)
            .uploadWithTransferUtility(
                this.context,
                "shallwebucket",
                "uploads",
                file,
                object : TransferListener {
                    override fun onStateChanged(id: Int, state: TransferState?) {
                    }
                    override fun onProgressChanged(id: Int, bytesCurrent: Long, bytesTotal: Long) {
                    }
                    override fun onError(id: Int, ex: java.lang.Exception?) {
                    }
                }
            );
        Log.d("S3Util", "hi")
        val uploadPhotoArray = UploadPhotoArray("uploads/$filename.$ext", giftData[giftIdx].idx)
        Log.d("upload memory photo array", "$uploadPhotoArray")
        postMemoryPhoto(uploadPhotoArray)
        Log.d("postMemoryPhoto", "${giftData[giftIdx].date} 날짜에 업로드 완료")
        val modDate: String = giftData[giftIdx].date.replace(".", "-")
        getMemoryPhoto(modDate)
    }

    // 버킷 내의 이미지를 해당 추억에 post
    private fun postMemoryPhoto(uploadPhotoArray: UploadPhotoArray) {
        RetrofitManager.instance.postMemoryPhoto(
            uploadPhotoArray = uploadPhotoArray,
            completion = { responseState ->
                when (responseState) {
                    RESPONSE_STATE.OKAY -> {
                        Log.d("retrofit", "postmemoryphoto api : ${responseState}")
                    }

                    RESPONSE_STATE.FAIL -> {
                        Log.d("retrofit", "api 호출 에러")
                    }
                }
            })
    }


    private fun initAlbum(resultData: ArrayList<MyAlbumPhotoData>) {
        adapter = MyAlbumAdapter(requireContext())
        binding.recyclerAlbumView.adapter = adapter
        adapter.setOnItemClickListener(this)

        val defaultImageUrl = listOf("add_photo.png")

        // Todo:  이부분도 List<String> 관련 수정 조치 필요
        albumData.apply {
            add(MyAlbumPhotoData(defaultImageUrl))
            addAll(resultData)
        }

        Log.d("retrofit", "initAlbum, $albumData")

        adapter.datas = albumData
        adapter.notifyDataSetChanged()
    }

    // user gift receive 를 통해 예약 정보를 받아옴
    private fun retrofitCallDate() {
        RetrofitManager.instance.usersGiftReceive(
            completion = { responseState, responseBody ->
                when (responseState) {
                    RESPONSE_STATE.OKAY -> {
                        Log.d("retrofit", "mygift api : ${responseBody?.size}")
                        getGiftDate(responseBody!!)
                    }

                    RESPONSE_STATE.FAIL -> {
                        Log.d("retrofit", "api 호출 에러")
                    }
                }
            }
        )
    }

    // 위의 retrofitCallDate에서 호출하는 함수. get memory-photo 결과 리스트 재정렬해서 giftData, dates에 저장
    private fun getGiftDate(resultData: ArrayList<MyGiftData>): List<String> {
        giftData = ArrayList(resultData)
        giftData.sortWith(compareByDescending<MyGiftData> { it.date.replace(".", "").toInt() }
//            .thenByDescending {
//            it.time.replace("시","").toInt()
//        } // Todo: response 변경으로 time 처리 다시 해야함.
        )
        // 클래스 데이터 2개 (cancellable이랑 message) 필요 없는데 없는 데이터클래스 생성 혹은 그냥 쓰기.?
        Log.d("retrofit", "original data: $resultData , replaced data: $giftData")
        dates =
            giftData.map { it.date } // 이게 굳이 있어야 하나 싶긴 함. giftData로 받아와서 title이랑 date 다 출력할 수 있지 않나 싶음...
        binding.tvAlbumDate.text = dates[giftIdx]

        return dates
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SuspiciousIndentation")
    private fun getMemoryPhoto(date: String) {
        RetrofitManager.instance.getMemoryPhoto(
            date = date
        ) { responseState, responseBody ->
            when (responseState) {
                RESPONSE_STATE.OKAY -> {
                    Log.d("retrofit", "myalbum api : ${responseBody?.size}")
                    responseBody?.forEach { myAlbumData ->
                        val photoUrls: List<String> = myAlbumData.memoryImgs.toList()
                        allPhotoData.add(MyAlbumPhotoData(photoUrls))
                        Log.d("get Memory Photo", "${responseBody}")
                    }
                    initAlbum(allPhotoData)
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
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            // 선택한 이미지의 Uri 가져오기
            selectedImageUri = data?.data!!
            path = selectedImageUri
            selectedImageUri =
                selectedImageUri?.let {
                    getImageAbsolutePath(
                        it,
                        requireContext()
                    )?.toUri()
                }!!// 선택한 이미지의 경로를 구하는 함수 호출
            if (selectedImageUri != null) {
                this.selectedImageUri = selectedImageUri
                parseUri(selectedImageUri)
                file = File(selectedImageUri.toString())
                upload()
            }
        }
    }

    private fun parseUri(selectedImageUri: Uri) {
        val fileForName = File(selectedImageUri.toString())
        filename = fileForName.nameWithoutExtension
        ext = fileForName.extension
//        Log.d("fileForName", "$fileForName")
//        Log.d("filename, ext", "$filename $ext")
//        Log.d("Album Result", "$selectedImageUri")

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
}

