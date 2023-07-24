package com.shall_we.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.shall_we.R
import com.shall_we.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {
    private lateinit var bannerViewPagerAdapter: BannerViewPagerAdapter
    private lateinit var viewModel : HomeFragmentViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentHomeBinding.inflate(inflater,container,false)

        viewModel = ViewModelProvider(this).get(HomeFragmentViewModel::class.java)
        viewModel.setBannerItems(
            listOf(
                BannerItem(R.drawable.banner_img),
                BannerItem(R.drawable.banner_img),
                BannerItem(R.drawable.banner_img),
                BannerItem(R.drawable.banner_img),
                BannerItem(R.drawable.banner_img),
                BannerItem(R.drawable.banner_img),
                BannerItem(R.drawable.banner_img),
                BannerItem(R.drawable.banner_img),
                BannerItem(R.drawable.banner_img),
                BannerItem(R.drawable.banner_img) //10개
                )
        )
        binding.viewPager2.apply {
            bannerViewPagerAdapter = BannerViewPagerAdapter()
            adapter = bannerViewPagerAdapter
            registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback(){
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    binding.tvPageNumber.text = "${position+1} | 10"
                }
            })
        }
        subscribeObservers()
        return binding.root
    }


    private fun subscribeObservers() {
        viewModel.bannerItemList.observe(viewLifecycleOwner, Observer { bannerItemList ->
            bannerViewPagerAdapter.submitList(bannerItemList)
        })
    }
}