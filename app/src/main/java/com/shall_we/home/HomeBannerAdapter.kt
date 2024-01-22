package com.shall_we.home

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.shall_we.R

class HomeBannerAdapter
        (private val itemList: List<BannerItem>) : RecyclerView.Adapter<HomeBannerAdapter.ViewHolder>() {

    private var currentPosition = 0
    private lateinit var pager: ViewPager2

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val viewPager2: ViewPager2 = itemView.findViewById(R.id.viewPager2)
        val tvPageNumber: TextView = itemView.findViewById(R.id.tv_page_number)
        // 여기에 다른 아이템에 대한 뷰를 찾는 코드 추가 가능
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_home_banner, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = itemList

        // 뷰페이저 어댑터 설정
        val bannerViewPagerAdapter = BannerViewPagerAdapter()
        pager = holder.viewPager2
        pager.apply {
            adapter = bannerViewPagerAdapter
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    holder.tvPageNumber.text = "${position + 1} | 2"
                }
            })
        }

        // 실제 데이터 설정
        bannerViewPagerAdapter.submitList(currentItem)

        //뷰페이저 넘기는 쓰레드
        val thread = Thread(PagerRunnable())
        thread.start()
    }

    val handler = Handler(Looper.getMainLooper()) {
        setPage()
        true
    }

    //2초 마다 페이지 넘기기
    inner class PagerRunnable : Runnable {
        override fun run() {
            while (true) {
                Thread.sleep(5000)
                handler.sendEmptyMessage(0)
            }
        }
    }

    //페이지 변경하기
    fun setPage() {
        if (currentPosition == 2) currentPosition = 0
        pager.setCurrentItem(currentPosition, true)
        currentPosition += 1
    }

    override fun getItemCount(): Int {
        return 1
    }
}


