package com.shall_we.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shall_we.R

class BannerViewPagerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        const val ITEM_COUNT = 10
    }

    private var bannerItemList: List<BannerItem>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return BannerViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_layout_banner, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return ITEM_COUNT
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        bannerItemList?.let { bannerItemList ->
            (holder as BannerViewHolder).bind(bannerItemList[position])
        }
    }

    //functions
    fun submitList(list: List<BannerItem>?) {
        bannerItemList = list
        notifyDataSetChanged()
    }


    //ViewHolder
    class BannerViewHolder
    constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imgProfile: ImageView = itemView.findViewById(R.id.iv_banner_image)

        fun bind(bannerItem: BannerItem) {
            Glide.with(itemView).load(bannerItem.image).into(imgProfile)

        }
    }
}