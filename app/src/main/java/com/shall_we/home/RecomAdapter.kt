package com.shall_we.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shall_we.R


class RecomAdapter(private val context: Context) : RecyclerView.Adapter<RecomAdapter.ViewHolder>() {
    var datas = mutableListOf<RecomData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_home_recom,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datas[position])

    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val txtName: TextView = itemView.findViewById(R.id.category_name)
        private val imgProfile: ImageView = itemView.findViewById(R.id.category_img)
        init {
            // 아이템 뷰에 클릭 리스너 설정
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    // 아이템 클릭 시 해당 포지션의 프래그먼트를 띄우기
                    val fragment = ProductListFragment()
                    val bundle = Bundle()
                    bundle.putString("tab", "추천경험")
                    bundle.putInt("position", position) // 클릭된 아이템의 이름을 "name" 키로 전달
                    fragment.arguments = bundle
                    val fragmentManager = (itemView.context as FragmentActivity).supportFragmentManager
                    fragmentManager.beginTransaction()
                        .add(R.id.home_layout, fragment)
                        .addToBackStack(null)
                        .commit()
                }
            }
        }
        fun bind(item: RecomData) {
            txtName.text = item.name
            Glide.with(itemView).load(item.img).into(imgProfile)
        }
    }

}

