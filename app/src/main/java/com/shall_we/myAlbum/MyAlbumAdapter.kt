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
import com.shall_we.R
import com.shall_we.databinding.ItemAlbumBinding
import com.shall_we.drawer.DrawerData
import java.io.File





class MyAlbumAdapter(private val context: Context) : RecyclerView.Adapter<MyAlbumAdapter.ViewHolder>() {
    var datas = mutableListOf<MyAlbumPhotoData>()

    private var itemClickListener: OnItemClickListener? = null

    // 클릭 리스너 인터페이스 정의
    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }
    fun setImageList(list: ArrayList<MyAlbumPhotoData>) {
        datas.addAll(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_album, parent, false)
//        val binding = ItemAlbumBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        val gridLayoutManager = GridLayoutManager(parent.context, 2)
//        binding.root.layoutManager = gridLayoutManager
//        setImageList()
        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = datas[position]

        // 여기서 데이터를 바인딩하여 ViewHolder에 표시합니다.
        // 예를 들어, ImageView에 이미지 설정하기
        // holder.binding.ivAlbum.setImageResource(data.imgUrl)

//        holder.root.setOnClickListener {
//            picDialog(it, position)
//        }

        holder.bind(data)
        holder.itemView.setOnClickListener {
            picDialog(it, position)

        }
    }

    fun picDialog(view: View, position: Int) {
        // 사진 추가
        if (position == 0) {
            Toast.makeText(view.context,    "사진 추가 기능", Toast.LENGTH_SHORT).show()
            itemClickListener?.onItemClick(position)

            //openGalleryForImageSelection()
        }

        // 사진 뷰어
        else {
            val myLayout = LayoutInflater.from(context).inflate(R.layout.dialog_photoviewer, null)
            val imageView = myLayout.findViewById<ImageView>(R.id.iv_photo)

            Glide.with(context).load(datas[position].imgUrl).into(imageView)

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
        return datas.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val ivAlbum: ImageView = itemView.findViewById(R.id.iv_album)
        @SuppressLint("SuspiciousIndentation")
        fun bind(item : MyAlbumPhotoData) {
            val position = adapterPosition

//            if (item.imgUrl) {
//                Glide.with(itemView).load(R.drawable.add_photo).into(ivAlbum)
            //}

            // 사진 뷰어
//            else {
//                Glide.with(itemView).load(datas[position].imgUrl).into(iv_album)
                val imgUrlList = datas[position].imgUrl
                Log.d("MyAlbumAdapter", "imgUrl $imgUrlList")
                for (imgUrl in imgUrlList) {
//                    val file = File(imgUrl)
//
//                    if (file.exists()) {
                    Glide.with(itemView).load(imgUrl).into(ivAlbum)
                    Log.d("MyAlbumAdapter", "imgUrl $imgUrl")
//                    } else {
//                        // 파일이 존재하지 않으면 이에 대해 적절히 처리합니다.
//                        Log.e("FileNotFoundError", "다음 경로에서 파일을 찾을 수 없습니다: " + file.absolutePath + imgUrl)
//                    }

               // }
                Log.d("MyAlbumAdapter", "memory photo 뷰어")
            }
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