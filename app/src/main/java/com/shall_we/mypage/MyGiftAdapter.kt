package com.shall_we.mypage

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.transition.TransitionManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.RecyclerView
import com.shall_we.R
import com.shall_we.databinding.ItemGiftBinding


class MyGiftAdapter(private val context: Context) : RecyclerView.Adapter<MyGiftAdapter.ViewHolder>() {
    var datas = mutableListOf<MyGiftDto>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemGiftBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = datas[position]
        holder.binding.tvDate.text = data.MyGift_date
        holder.binding.tvTime.text = data.MyGift_time
        holder.binding.tvTitle.text = data.MyGift_title
        holder.binding.tvDescription.text = data.MyGift_description
        holder.binding.tvMessage.text = data.MyGift_message
        holder.binding.tvDate.setCompoundDrawablesRelativeWithIntrinsicBounds(
            holder.itemView.context.getDrawable(R.drawable.calendar_light_resize), // 시작 부분 Drawable 설정
            null,     // 위쪽 Drawable 설정 (null이면 이전 설정 유지)
            null,     // 끝 부분 Drawable 설정 (null이면 이전 설정 유지)
            null      // 아래쪽 Drawable 설정 (null이면 이전 설정 유지)
        )
        holder.binding.tvTime.setCompoundDrawablesRelativeWithIntrinsicBounds(
            holder.itemView.context.getDrawable(R.drawable.time_light_resize), // 시작 부분 Drawable 설정
            null,     // 위쪽 Drawable 설정 (null이면 이전 설정 유지)
            null,     // 끝 부분 Drawable 설정 (null이면 이전 설정 유지)
            null      // 아래쪽 Drawable 설정 (null이면 이전 설정 유지)
        )
        if (data.MyGift_cancellation == false)
            holder.binding.tvChangeReserv.visibility = View.GONE


        if (position == 1)
            holder.binding.ivMessage.setImageResource(R.drawable.message_img2)
        // 클릭 이벤트 처리
        holder.binding.cardView.setOnClickListener {
            if (holder.binding.tvMessage.visibility == View.VISIBLE) {
                changeColorContacted(holder, position)
            } else {
                changeColorExpanded(holder, position)

                // 이미지 로딩 등의 작업 수행
                // Glide, Picasso 등의 라이브러리를 사용하여 이미지 로딩
                // 예시: Glide.with(holder.itemView).load(data.imageUrl).into(holder.binding.imageView)
            }
        }
    }
    private fun changeColorContacted(holder: ViewHolder, position: Int) {
        val data = datas[position]
        holder.binding.tvMessage.visibility = View.GONE
        holder.binding.ivMessage.visibility = View.GONE
        holder.binding.tvChangeReserv.visibility = View.GONE
        holder.binding.constView.setBackgroundColor(Color.parseColor("#F8F8F8"))
        holder.binding.tvDate.setCompoundDrawablesRelativeWithIntrinsicBounds(
            holder.itemView.context.getDrawable(R.drawable.calendar_black_resize), // 시작 부분 Drawable 설정
            null,     // 위쪽 Drawable 설정 (null이면 이전 설정 유지)
            null,     // 끝 부분 Drawable 설정 (null이면 이전 설정 유지)
            null      // 아래쪽 Drawable 설정 (null이면 이전 설정 유지)
        )
        holder.binding.tvTime.setCompoundDrawablesRelativeWithIntrinsicBounds(
            holder.itemView.context.getDrawable(R.drawable.time_black_resize), // 시작 부분 Drawable 설정
            null,     // 위쪽 Drawable 설정 (null이면 이전 설정 유지)
            null,     // 끝 부분 Drawable 설정 (null이면 이전 설정 유지)
            null      // 아래쪽 Drawable 설정 (null이면 이전 설정 유지)
        )
        holder.binding.tvDate.setBackgroundResource(R.drawable.tv_date_unselected)
        holder.binding.tvTime.setBackgroundResource(R.drawable.tv_date_unselected)
        holder.binding.tvDate.setTextColor(Color.parseColor("#333333"))
        holder.binding.tvTime.setTextColor(Color.parseColor("#333333"))
    }
    private fun changeColorExpanded(holder: ViewHolder, position: Int) {
        val data = datas[position]
        holder.binding.tvMessage.visibility = View.VISIBLE
        holder.binding.ivMessage.visibility = View.VISIBLE
        if (data.MyGift_cancellation == true)
            holder.binding.tvChangeReserv.visibility = View.VISIBLE
        holder.binding.constView.setBackgroundColor(Color.parseColor("#FFF5F6"))
        holder.binding.tvDate.setCompoundDrawablesRelativeWithIntrinsicBounds(
            holder.itemView.context.getDrawable(R.drawable.calendar_light_resize), // 시작 부분 Drawable 설정
            null,     // 위쪽 Drawable 설정 (null이면 이전 설정 유지)
            null,     // 끝 부분 Drawable 설정 (null이면 이전 설정 유지)
            null      // 아래쪽 Drawable 설정 (null이면 이전 설정 유지)
        )
        holder.binding.tvTime.setCompoundDrawablesRelativeWithIntrinsicBounds(
            holder.itemView.context.getDrawable(R.drawable.time_light_resize), // 시작 부분 Drawable 설정
            null,     // 위쪽 Drawable 설정 (null이면 이전 설정 유지)
            null,     // 끝 부분 Drawable 설정 (null이면 이전 설정 유지)
            null      // 아래쪽 Drawable 설정 (null이면 이전 설정 유지)
        )
        holder.binding.tvDate.setBackgroundResource(R.drawable.tv_date_selected)
        holder.binding.tvTime.setBackgroundResource(R.drawable.tv_date_selected)
        holder.binding.tvDate.setTextColor(Color.parseColor("#E31B54"))
        holder.binding.tvTime.setTextColor(Color.parseColor("#E31B54"))
    }


    override fun getItemCount(): Int {
        return datas.size
    }

    inner class ViewHolder(val binding: ItemGiftBinding) : RecyclerView.ViewHolder(binding.root){
        init {
            // 예약 수정으로 이동
            binding.tvChangeReserv.setOnClickListener {
                // 클릭한 TextView의 위치(position)을 가져옴
                val position = adapterPosition

                // 해당 위치에 있는 아이템 데이터 가져오기
                val itemData = datas[position]

//                // 클릭한 아이템 데이터 처리 (예: 다른 화면으로 이동 등)
//                // 특정 프래그먼트로 이동하려면 findNavController()를 사용하여 목적지로 이동
//                // 예를 들어, 목적지의 id가 myAlbumFragment인 경우:
//                val action = MyGiftReceivedFragmentDirections.actionMyGiftReceivedFragmentToMyAlbumFragment()
//                findNavController().navigate(action)

//                val myGiftReceivedFragment = MyGiftReceivedFragment()
//                val fragmentTransaction = parentFragmentManager.beginTransaction()
//                fragmentTransaction.replace(R.id.mypage_layout, myAlbumFragment, "myAlbumFragment")
//                fragmentTransaction.addToBackStack(null)
//                fragmentTransaction.commitAllowingStateLoss()
//                Log.d("clicked","change")
            }

            // 예약 취소로 이동
            binding.tvCancelReserv.setOnClickListener {
            }
        }
    }
}
