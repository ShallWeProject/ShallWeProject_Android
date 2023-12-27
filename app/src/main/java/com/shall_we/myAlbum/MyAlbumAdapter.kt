package com.shall_we.myAlbum

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
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.shall_we.R
import com.shall_we.retrofit.RESPONSE_STATE
import com.shall_we.retrofit.RetrofitManager


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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_album, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = datas.myPhoto?.get(position)?.memoryPhotoImgUrl

        if (position == 0) {
            // 맨 처음 아이템일 때 이미지 추가 버튼 로드
            Glide.with(context)
                .load(R.drawable.add_photo)
                .into(holder.ivAlbum)
        } else if (position < datas.myPhoto?.size ?: 0) {
            // 나머지 아이템은 실제 데이터의 이미지 로드
            Glide.with(context)
                .load(data)
                .apply(RequestOptions.placeholderOf(R.drawable.baking))
                .into(holder.ivAlbum)
        }

        holder.itemView.setOnClickListener {
            picDialog(it, position, datas)

        }
    }

    private fun picDialog(view: View, position: Int, data: MyAlbumPhotoData) {
        // 사진 추가
        if (position == 0) {
            itemClickListener?.onItemClick(position)

        }

        // 사진 뷰어
        else {
            val myLayout = LayoutInflater.from(context).inflate(R.layout.dialog_photoviewer, null)
            val imageView = myLayout.findViewById<ImageView>(R.id.iv_photo)
            val delbtn = myLayout.findViewById<Button>(R.id.btn_delete)
            if (data.myPhoto?.get(position)?.isUploader == false)
                delbtn.visibility = View.GONE
            else
                delbtn.visibility = View.VISIBLE

            Glide.with(context).load(data.myPhoto?.get(position)?.memoryPhotoImgUrl).into(imageView)

            val build = AlertDialog.Builder(view.context).apply {
                setView(myLayout)
            }
            val dialog = build.create()
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            dialog.show()

            myLayout.findViewById<Button>(R.id.btn_delete).setOnClickListener {
                retrofitDelApiCall(datas.myPhoto?.get(position)!!.memoryPhotoImgUrl)
                Log.d("삭제할 이미지 url: ", "${datas.myPhoto?.get(position)!!.memoryPhotoImgUrl}")
                datas.myPhoto?.removeAt(position)
                dialog.dismiss()
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, itemCount)
            }

            myLayout.findViewById<Button>(R.id.btn_close).setOnClickListener {
                dialog.dismiss()
            }
        }
    }

    private fun retrofitDelApiCall(url : String) {
        RetrofitManager.instance.deleteMemoryPhoto(photoUrl = url) { responseState, responseBody ->
            when (responseState) {
                RESPONSE_STATE.OKAY -> {
                    Log.d("retrofit", "$responseState")
                }

                RESPONSE_STATE.FAIL -> {
                    Log.d("retrofit", "api 호출 에러")
                }
            }
        }
    }


    override fun getItemCount(): Int {
        return datas.myPhoto?.size ?: 0
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivAlbum: ImageView = itemView.findViewById(R.id.iv_album)
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