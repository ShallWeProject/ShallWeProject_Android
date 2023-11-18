package com.shall_we.myAlbum

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.shall_we.databinding.FragmentMyAlbumBinding
import com.shall_we.mypage.MyGiftData
import com.shall_we.retrofit.BodyData
import com.shall_we.retrofit.RESPONSE_STATE
import com.shall_we.retrofit.RetrofitManager
import com.shall_we.retrofit.UploadPhotoArray
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class MyAlbumFragment : Fragment() ,MyAlbumAdapter.OnItemClickListener {
    private lateinit var binding: FragmentMyAlbumBinding
    private lateinit var adapter: MyAlbumAdapter

    private val albumData = ArrayList<MyAlbumPhotoData>()
    private val allPhotoData: ArrayList<MyAlbumPhotoData> = ArrayList()
    private lateinit var giftData: ArrayList<MyGiftData>
    private var giftIdx = 0

    private lateinit var imageKey: String
    private lateinit var presignedUrl: String

    private lateinit var dates : List<String>
    private var selectedImageUri: Uri= Uri.EMPTY
    private var filename: String = ""
    private var ext: String=""
    private lateinit var file:File

    private var path: Uri= Uri.EMPTY


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermission()
        RetrofitCallDate()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyAlbumBinding.inflate(inflater, container, false)
//        (activity as AppCompatActivity).findViewById<ExtendedFloatingActionButton>(R.id.fab_album).show()

        binding.ivLeft.setOnClickListener {
            if (giftData.size>giftIdx+1){
                giftIdx+=1
                binding.tvAlbumDate.text = dates[giftIdx]
                binding.tvAlbumDescription.text = giftData[giftIdx].description
                binding.tvAlbumTitle.text = giftData[giftIdx].title
                Log.d("album date", "album date ++ is not null")
            }
            else {
                Log.d("album date", "album date ++ is null, ${binding.tvAlbumDate.text}")
            }
        }


        binding.ivRight.setOnClickListener {
            if (giftIdx>=1){
                giftIdx-=1
                binding.tvAlbumDate.text = dates[giftIdx]
                binding.tvAlbumDescription.text = giftData[giftIdx].description
                binding.tvAlbumTitle.text = giftData[giftIdx].title
                Log.d("album date", "album date -- is not null")
            }
            else {
                Log.d("album date", "album date -- is null, ${binding.tvAlbumDate.text}")
            }
        }


        return binding.root
    }

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

//        RetrofitCall(formattedDateTime)
    }

    private fun initAlbum(resultData: ArrayList<MyAlbumPhotoData>) {
        adapter = MyAlbumAdapter(requireContext())
        binding.recyclerAlbumView.adapter = adapter
        adapter.setOnItemClickListener(this)

        val defaultImageUrl = listOf("add_photo.png")

        albumData.apply {
            add(MyAlbumPhotoData(defaultImageUrl))
            addAll(resultData)
        }

        Log.d("retrofit","initAlbum, $albumData")

        adapter.datas = albumData
        adapter.notifyDataSetChanged()
    }

    // get memory-photo 결과 리스트 재정렬해서 giftData, dates에 저장
    private fun getGiftDate(resultData: ArrayList<MyGiftData>) : List<String> {
        giftData = ArrayList(resultData)
        giftData.sortWith(compareByDescending<MyGiftData> { it.date.replace(".","").toInt() }.thenByDescending { it.time.replace("시","").toInt() })
        // 클래스 데이터 2개 (cancellable이랑 message) 필요 없는데 없는 데이터클래스 생성 혹은 그냥 쓰기.?
        Log.d("retrofit", "여기까지오션나요..?여기는 getGiftDate입니닷.^^ 원래 data는 $resultData 이고 수정된 data는 $giftData")
        dates = giftData.map { it.date } // 이게 굳이 있어야 하나 싶긴 함. giftData로 받아와서 title이랑 date 다 출력할 수 있지 않나 싶음...
        binding.tvAlbumDate.text = dates[giftIdx]

        return dates

    }


    private fun RetrofitCallDate() {
        RetrofitManager.instance.usersGiftSend(
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
            })
    }

    @SuppressLint("SuspiciousIndentation")
    private fun getMemoryPhoto(date: String) {
        RetrofitManager.instance.getMemoryPhoto(
            date = date,
            completion = { responseState, responseBody ->

                when (responseState) {
                    RESPONSE_STATE.OKAY -> {
                        Log.d("retrofit", "myalbum api : ${responseBody?.size}")
                        responseBody?.forEach { myAlbumData ->
                            val photoUrls: List<String> = myAlbumData.memoryImgs.toList()
                                allPhotoData.add(MyAlbumPhotoData(photoUrls))
                        }

                        initAlbum(allPhotoData)
                    }

                    RESPONSE_STATE.FAIL -> {
                        Log.d("retrofit", "api 호출 에러")
                    }
                }
            })
    }


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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                requireActivity().startActivity(intent)
            }
        }
    }
    private val PICK_IMAGE_REQUEST = 1 // 요청 코드

    // 갤러리 열기
    private fun openGallery() {
        Log.d("gallery","갤러리")
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    // startActivityForResult로부터 결과 처리
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            // 선택한 이미지의 Uri 가져오기
            selectedImageUri = data?.data!!
            System.out.println("selectedImageUri = "+selectedImageUri)
            path = selectedImageUri
            selectedImageUri =
                selectedImageUri?.let { getImageAbsolutePath(it, requireContext())?.toUri() }!!// 선택한 이미지의 경로를 구하는 함수 호출
            // newfile = File(selectedImageUri)
            // Uri를 사용하여 이미지를 처리하거나 표시할 수 있습니다.
            if (selectedImageUri != null) {
                this.selectedImageUri = selectedImageUri
                parseUri(selectedImageUri)
                file = File(selectedImageUri.toString())
                getImgUrl(ext, "uploads", filename, file)

            }

        }
    }

    private fun parseUri(selectedImageUri: Uri) {
//        Toast.makeText(view?.context, "이미지의 URI는 $selectedImageUri 입 니 다", Toast.LENGTH_SHORT).show()
        Log.d("Album Result", "$selectedImageUri")
        val fileForName = File(selectedImageUri.toString())
        filename = fileForName.nameWithoutExtension
        ext = fileForName.extension
        Log.d("filename, ext", "$filename $ext")




    }
    private fun getImgUrl(ext: String, dir: String, filename: String, file:File) {
        val data = BodyData(ext = ext, dir = dir, filename = filename)

        Log.d("data","$data")
        RetrofitManager.instance.getImgUrl(data,
            completion = { responseState, imageKey, presignedUrl ->
                when (responseState) {
                    RESPONSE_STATE.OKAY -> {
                        Log.d("retrofit", "getImgUrl api : ${responseState}")
                        //API.UPLOAD_IMG=presignedUrl
                        getImgUrlResult(imageKey, presignedUrl)

                        // 파일의 MIME 유형 가져오기 (예: text/plain, image/jpeg 등)
                        val mediaType = "image/*".toMediaTypeOrNull() // 파일의 MIME 유형에 따라 변경

                        // RequestBody로 파일을 래핑
                        val requestBody = file.asRequestBody(mediaType)

                        // 파일을 MultipartBody.Part 형식으로 변환
                        val filePart = MultipartBody.Part.createFormData("file", file.name, requestBody)

                        uploadImg(filePart)

                    }

                    RESPONSE_STATE.FAIL -> {
                        Log.d("retrofit", "api 호출 에러")
                    }
                }
            })
    }
    private fun getImgUrlResult(imageKey: String, presignedUrl: String) {
        this.imageKey = imageKey
        Log.d("First presignedUrl","$presignedUrl")
        this.presignedUrl = presignedUrl.substringAfter("https://shallwebucket.s3.ap-northeast-2.amazonaws.com/")
        Log.d("presignedUrl","${this.presignedUrl}")

    }

    private fun uploadImg(image: MultipartBody.Part) {
        RetrofitManager.instance.uploadImg(
            image = image,url="https://shallwebucket.s3.ap-northeast-2.amazonaws.com/", endPoint = this.presignedUrl,
            completion = { responseState ->
                when (responseState) {
                    RESPONSE_STATE.OKAY -> {
                        Log.d("retrofit", "uploadImg api : ${responseState}")
                    }

                    RESPONSE_STATE.FAIL -> {
                        Log.d("retrofit", "uploadImg api 호출 에러")
                    }
                }
                val uploadPhotoArray = UploadPhotoArray(imageKey, giftData[giftIdx].idx)
                Log.d("post Memory Photo", "$uploadPhotoArray")
                postMemoryPhoto(uploadPhotoArray)
                val modDate = giftData[giftIdx].date.replace(".","-")
                getMemoryPhoto(modDate)
            })

//            var selectedImageUri: Uri? = data?.data
//            selectedImageUri =
//                selectedImageUri?.let { getImageAbsolutePath(it, requireContext())?.toUri() }// 선택한 이미지의 경로를 구하는 함수 호출

            // Uri를 사용하여 이미지를 처리하거나 표시할 수 있습니다.
            if (selectedImageUri != null) {
                Log.d("Album Result", "$selectedImageUri")
            }
        }

    override fun onItemClick() {
        // 클릭된 아이템이 첫 번째 아이템(사진 추가 버튼)일 때
        openGallery()


    }
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
                    val imageFile18 = File(path)
                    //Todo: 여기가 imageFile
                    Log.d("getImageAbsolutePath", "절대경로 추출 uri = $path")
                }
            }
        } catch (e: Exception) {
            // 오류 처리를 여기에 추가하십시오.
            Log.e("getImageAbsolutePath", "절대경로 추출 오류: ${e.message}")
        } finally {
            cursor?.close()
        }

        return path
    }