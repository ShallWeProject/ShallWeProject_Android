package com.shall_we.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shall_we.R

class ProductAdapter(private val context: Context) : RecyclerView.Adapter<ProductAdapter.ViewHolder>() {
    var datas = mutableListOf<ProductData>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false)


        return ViewHolder(view)
    }

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
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
            txtName.text = item.name
            txtComment.text = item.comment
            txtPrice.text = item.price

            Glide.with(itemView).load(item.img).into(imgProfile)

        }
    }

}

