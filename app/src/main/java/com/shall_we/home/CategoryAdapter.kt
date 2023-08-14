package com.shall_we.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.shall_we.R


class CategoryAdapter (private val context: Context) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {
    var datas = mutableListOf<CategoryData>()
    private var selectedItemIndex = 0 // 선택된 아이템의 인덱스

    private var itemClickListener: OnItemClickListener? = null

    // 클릭 리스너 인터페이스 정의
    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_realtime_category,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bind(datas[position])
        // 버튼 배경 설정
        val buttonBg = if (selectedItemIndex == position) {
            R.drawable.category_btn_selected // 선택된 상태 배경
        } else {
            R.drawable.category_btn // 기본 상태 배경
        }
        holder.categoryBtn.setBackgroundResource(buttonBg)

        // 아이템 클릭 이벤트 처리
        holder.categoryBtn.setOnClickListener {
            // 선택된 아이템의 인덱스를 설정하고 변경을 알림
            setSelectedItemIndex(position)
            itemClickListener?.onItemClick(position)
            notifyDataSetChanged()
        }
    }
    fun setSelectedItemIndex(index: Int) {
        selectedItemIndex = index
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val categoryBtn: Button = itemView.findViewById(R.id.category_btn)

        fun bind(item: CategoryData) {
            categoryBtn.text = item.name
        }
    }



}
