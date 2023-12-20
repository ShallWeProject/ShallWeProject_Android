package com.shall_we.giftExperience

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.shall_we.R
import com.shall_we.home.CategoryAdapter
import com.shall_we.home.CategoryData

class ReservationTimeAdapter(private val context: Context):RecyclerView.Adapter<ReservationTimeAdapter.ViewHolder>() {
    var datas = mutableListOf<ReservationTimeData>()
    private var selectedItemIndex = -1 // 선택된 아이템의 인덱스

    private var itemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(position: Int, text: String)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_reservation_time,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datas[position])

        val selectedColor = ContextCompat.getColor(context, R.color.white)
        val defaultColor = Color.parseColor("#EDA5B2")

        // 버튼 배경 설정
        val buttonBg = if (selectedItemIndex == position) {
            R.drawable.reservation_time_selected // 선택된 상태 배경
        } else {
            R.drawable.reservation_time // 기본 상태 배경
        }
        holder.categoryBtn.setBackgroundResource(buttonBg)

        // 아이템 클릭 이벤트 처리
        holder.categoryBtn.setOnClickListener {
            // 이전에 선택된 아이템의 텍스트와 배경 색을 복원
            val prevItem = selectedItemIndex // 이전에 선택된 아이템 인덱스 저장
            if (selectedItemIndex != -1 && selectedItemIndex != position) {
                selectedItemIndex = -1 // 이전에 선택된 아이템 인덱스 초기화
                notifyItemChanged(prevItem) // 이전에 선택된 아이템 갱신
            }

            // 선택된 아이템의 인덱스를 설정하고 변경을 알림
            setSelectedItemIndex(position)
            itemClickListener?.onItemClick(position, datas[position].time)
            notifyDataSetChanged()

            // 아이템 선택 여부에 따라 배경과 텍스트 색상 변경
            if (selectedItemIndex == position) {
                holder.categoryBtn.setBackgroundColor(R.drawable.reservation_time_selected)
                holder.categoryBtn.setTextColor(selectedColor)
            } else if(prevItem == position) {
                holder.categoryBtn.setBackgroundResource(R.drawable.reservation_time)
                holder.categoryBtn.setTextColor(defaultColor)
            }
        }

        // 아이템이 선택되었을 때 텍스트와 배경 색 변경
        if (selectedItemIndex == position) {
            holder.categoryBtn.setTextColor(selectedColor)
        }else{
            holder.categoryBtn.setTextColor(defaultColor)
        }

    }
    fun setSelectedItemIndex(index: Int) {
        selectedItemIndex = index
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val categoryBtn: Button = itemView.findViewById(R.id.btn_time)

        fun bind(item: ReservationTimeData) {
            categoryBtn.text = item.time+"시"
        }
    }
}