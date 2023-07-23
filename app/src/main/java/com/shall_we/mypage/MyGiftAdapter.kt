package com.shall_we.mypage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.shall_we.databinding.ItemGiftBinding


class MyGiftAdapter(private val dataList: List<MyGiftDto>) : RecyclerView.Adapter<MyGiftAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemGiftBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = dataList[position]

        holder.binding.tvDate.text = data.MyGift_idx.toString()
        holder.binding.tvTime.text = data.MyGift_title

        // 클릭 이벤트 처리
        holder.binding.cardView.setOnClickListener {
            if (holder.binding.tvMessage.visibility == View.VISIBLE) {
                holder.binding.tvMessage.visibility = View.GONE
                holder.binding.ivMessage.visibility = View.GONE
                holder.binding.tvChangeReserv.visibility = View.GONE
            } else {
                holder.binding.tvMessage.visibility = View.VISIBLE
                holder.binding.ivMessage.visibility = View.VISIBLE
                holder.binding.tvChangeReserv.visibility = View.VISIBLE

                // 이미지 로딩 등의 작업 수행
                // Glide, Picasso 등의 라이브러리를 사용하여 이미지 로딩
                // 예시: Glide.with(holder.itemView).load(data.imageUrl).into(holder.binding.imageView)
            }
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class ViewHolder(val binding: ItemGiftBinding) : RecyclerView.ViewHolder(binding.root)
}
