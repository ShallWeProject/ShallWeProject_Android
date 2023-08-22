package com.shall_we.myAlbum

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shall_we.R
import com.shall_we.databinding.FragmentMyAlbumBinding
import com.shall_we.databinding.ItemAlbumBinding
import com.shall_we.home.ProductData


class MyAlbumAdapter(private val context: Context) : RecyclerView.Adapter<MyAlbumAdapter.ViewHolder>() {
    var datas = ArrayList<MyAlbumPhotoData>()

    private var itemClickListener: OnItemClickListener? = null

    // 클릭 리스너 인터페이스 정의
    interface OnItemClickListener {
        fun onItemClick()
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }
    fun setImageList(list: ArrayList<MyAlbumPhotoData>) {
        datas.addAll(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAlbumBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        val gridLayoutManager = GridLayoutManager(parent.context, 2)
//        binding.root.layoutManager = gridLayoutManager
        return ViewHolder(binding)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = datas[position]

        // 여기서 데이터를 바인딩하여 ViewHolder에 표시합니다.
        // 예를 들어, ImageView에 이미지 설정하기
        // holder.binding.ivAlbum.setImageResource(data.imgUrl)

        holder.binding.root.setOnClickListener {
            picDialog(it, position)
        }

        holder.bind(datas[position])
    }

    fun picDialog(view: View, position: Int) {
        // 사진 추가
        if (position == 0) {
            Toast.makeText(view.context,    "사진 추가 기능", Toast.LENGTH_SHORT).show()
            itemClickListener?.onItemClick()

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


        }
    }


    override fun getItemCount(): Int {
        return datas.size
    }

    inner class ViewHolder(val binding: ItemAlbumBinding) : RecyclerView.ViewHolder(binding.root) {
        private val iv_album: ImageView = itemView.findViewById(R.id.iv_album)
        fun bind(item : MyAlbumPhotoData) {
            val position = adapterPosition

            if (position == 0) {
                Glide.with(context).load(R.drawable.add_photo).into(iv_album)
            }

            // 사진 뷰어
            else {
                Glide.with(context).load(datas[position].imgUrl).into(iv_album)
            }
//            val item = datas[absoluteAdapterPosition]
//            Glide.with(itemView)
//                .load(item)
//                .into(binding.ivAlbum)
        }
    }
}