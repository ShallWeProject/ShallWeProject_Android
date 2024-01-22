package com.shall_we.home

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shall_we.ExperienceDetail.ExperienceDetailFragment
import com.shall_we.R

class ProductAdapter(private val context: Context) : RecyclerView.Adapter<ProductAdapter.ViewHolder>() {
    var datas = mutableListOf<ProductData>()
    private var itemClickListener: OnItemClickListener? = null

    // 클릭 리스너 인터페이스 정의
    interface OnItemClickListener {
        fun onItemClick(item: ProductData)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false)


        return ViewHolder(view)
    }

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d("RecyclerView", "onBindViewHolder called for position: $position")

        holder.bind(datas[position])

    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val constraintLayout: ConstraintLayout = itemView.findViewById(R.id.constraintLayout)
        val card : CardView = itemView.findViewById(R.id.card)
        private val txtName: TextView = itemView.findViewById(R.id.product_name)
        private val txtComment: TextView = itemView.findViewById(R.id.product_comment)
        private val txtPrice: TextView = itemView.findViewById(R.id.product_price)
        val imgProfile: ImageView = itemView.findViewById(R.id.product_image)

        fun bind(item: ProductData) {
            txtName.text = item.subtitle
            txtComment.text = item.title
            txtPrice.text = item.price

            if(item.img == ""){
                imgProfile.setImageResource(R.drawable.splash_icon)
            }
            else{
                Glide.with(context).load(item.img).into(imgProfile)
            }

        }
        init {
            // itemView를 클릭했을 때 해당 항목의 ProductData를 클릭 리스너를 통해 전달
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = datas[position]
                    itemClickListener?.onItemClick(item)
                    navigateToExperienceDetail(item)
                }
            }
        }
    }
    fun navigateToExperienceDetail(item: ProductData) {
        val newFragment = ExperienceDetailFragment()
        val bundle = Bundle()
        bundle.putInt("id", item.giftid)
        newFragment.arguments = bundle
        // Get the activity from the context
        val activity = context as? FragmentActivity
        activity?.supportFragmentManager?.beginTransaction()
            ?.add(R.id.nav_host_fragment, newFragment)  // Use replace instead of add
            ?.addToBackStack(null)?.commit()
    }

    override fun onViewRecycled(holder: ViewHolder) {
        super.onViewRecycled(holder)
        Log.d("RecyclerView", "onViewRecycled called for position: ${holder.adapterPosition}")
    }
}

