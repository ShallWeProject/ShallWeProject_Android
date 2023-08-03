package com.shall_we.Drawer

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shall_we.R
import com.shall_we.home.ProductListFragment
import com.shall_we.home.RecomAdapter
import com.shall_we.home.RecomData

class DrawerAdapter (private val context: Context) : RecyclerView.Adapter<DrawerAdapter.ViewHolder>() {
    var datas = mutableListOf<DrawerData>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_drawer_menu,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datas[position])

    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val txtName: TextView = itemView.findViewById(R.id.category_text)
        private val imgProfile: ImageView = itemView.findViewById(R.id.category_icon)
        init {
            // 아이템 뷰에 클릭 리스너 설정
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    // 아이템 클릭 시 해당 포지션의 프래그먼트를 띄우기
                    val fragment = ProductListFragment.newInstance(position)
                    val fragmentManager = (itemView.context as FragmentActivity).supportFragmentManager
                    fragmentManager.beginTransaction()
                        .replace(R.id.nav_host_fragment, fragment)
                        .addToBackStack(null)
                        .commit()
                }
            }
        }
        fun bind(item: DrawerData) {
            txtName.text = item.name
            Glide.with(itemView).load(item.img).into(imgProfile)

        }
    }

}
data class DrawerData (
    val name : String,
    val img : Int,
)
