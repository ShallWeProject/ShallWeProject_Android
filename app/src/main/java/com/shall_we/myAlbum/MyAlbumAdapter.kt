package com.shall_we.myAlbum

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.shall_we.R
import java.io.File





class MyAlbumAdapter(private val context: Context) : RecyclerView.Adapter<MyAlbumAdapter.ViewHolder>() {
    lateinit var datas : MyAlbumPhotoData

    private var itemClickListener: OnItemClickListener? = null

    // 클릭 리스너 인터페이스 정의
    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }
//    fun setImageList(list: ArrayList<MyAlbumPhotoData>) {
//        datas.addAll(list)
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_album, parent, false)
//        val binding = ItemAlbumBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        val gridLayoutManager = GridLayoutManager(parent.context, 2)
//        binding.root.layoutManager = gridLayoutManager
//        setImageList()
        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val data = datas.imgUrl?.get(position)

        if (position == 0) {
            // 맨 처음 아이템일 때 정해진 이미지 로드
            Glide.with(context)
                .load(R.drawable.add_photo)
                .apply(RequestOptions.placeholderOf(R.drawable.baking))
                .into(holder.ivAlbum)
        } else if (position < datas.imgUrl?.size ?: 0) {
            // 나머지 아이템은 실제 데이터의 이미지 로드
            Glide.with(context)
                .load(data)
                .apply(RequestOptions.placeholderOf(R.drawable.baking))
                .into(holder.ivAlbum)
        }

//        holder.bind(datas)
        holder.itemView.setOnClickListener {
            picDialog(it, position, datas)

        }
    }

    private fun picDialog(view: View, position: Int, data: MyAlbumPhotoData) {
        // 사진 추가
        if (position == 0) {
//            Toast.makeText(view.context, "사진 추가 기능", Toast.LENGTH_SHORT).show()
            itemClickListener?.onItemClick(position)

            //openGalleryForImageSelection()
        }

        // 사진 뷰어
        else {
            val myLayout = LayoutInflater.from(context).inflate(R.layout.dialog_photoviewer, null)
            val imageView = myLayout.findViewById<ImageView>(R.id.iv_photo)

            Glide.with(context).load(data.imgUrl?.get(position)).into(imageView)

            val build = AlertDialog.Builder(view.context).apply {
                setView(myLayout)
            }
            val dialog = build.create()
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            dialog.show()

            myLayout.findViewById<Button>(R.id.btn_close).setOnClickListener {
                dialog.dismiss()
            }
        }
    }


    override fun getItemCount(): Int {
        return datas.imgUrl?.size ?: 0
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivAlbum: ImageView = itemView.findViewById(R.id.iv_album)
        @SuppressLint("SuspiciousIndentation")
        fun bind(item : MyAlbumPhotoData) {
            val position = adapterPosition

//            if (item.imgUrl) {
//                Glide.with(itemView).load(R.drawable.add_photo).into(ivAlbum)
            //}

            // 사진 뷰어
//            else {
//                Glide.with(itemView).load(datas[position].imgUrl).into(iv_album)

//            val imgUrlList = datas[position].imgUrl
            val imgUrlList = item.imgUrl
            Log.d("MyAlbumAdapter", "imgUrl list $imgUrlList")
            if (imgUrlList != null && imgUrlList.isNotEmpty()) {
                // 첫 번째 이미지 URL을 기본값으로 설정
                val firstImageUrl = imgUrlList[0]

                // 첫 번째 이미지 URL을 플레이스홀더로 설정하고 로드 시작
                Glide.with(itemView)
                    .load(firstImageUrl)
                    .apply(RequestOptions.placeholderOf(R.drawable.baking))
                    .into(ivAlbum)

                // 나머지 이미지 URL에 대한 처리
                for (i in 1 until imgUrlList.size) {
                    val imageUrl = imgUrlList[i]
                    Glide.with(itemView)
                        .load(imageUrl)
//                        .transform(CenterCrop(), RoundedCorners(30))
//                        .apply(RequestOptions.bitmapTransform(RoundedCorners(80)))
                        .apply(RequestOptions.placeholderOf(R.drawable.baking))
                        //.into(ivAlbum)
                        .preload() // 이미지를 미리 로드
                    Log.d("MyAlbumAdapter", "imgUrl $imageUrl")

                }
//            }
//            if (imgUrlList != null) {
//                for (imgUrl in imgUrlList) {
//                    val file = File(imgUrl)
//
//                    //if (file.exists()) {
//                    Glide.with(itemView).load(imgUrl).apply(RequestOptions.placeholderOf(R.drawable.baking)) // 플레이스홀더를 지정합니다.
//                        .into(ivAlbum)
//                    Log.d("MyAlbumAdapter", "imgUrl $imgUrl")
//        //                    } else {
//        //                        // 파일이 존재하지 않으면 이에 대해 적절히 처리합니다.
//        //                        Log.e("FileNotFoundError", "다음 경로에서 파일을 찾을 수 없습니다: " + imgUrl) //+ file.absolutePath
//        //                    }
//
//                    // }
//                }
            }
            Log.d("MyAlbumAdapter", "memory photo 뷰어")
        }

        init {
            // itemView를 클릭했을 때 해당 항목의 ProductData를 클릭 리스너를 통해 전달
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    itemClickListener?.onItemClick(position)
                }
            }
        }
    }
}