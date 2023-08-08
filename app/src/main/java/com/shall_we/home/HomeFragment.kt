package com.shall_we.home

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.shall_we.R
import com.shall_we.search.SearchFragment
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

        binding.mainSearchView.clearFocus()
        val hintColor = Color.parseColor("#FEA3B4") // 원하는 힌트 색상으로 변경
        val searchEditText =
            binding.mainSearchView.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)
        searchEditText.setHintTextColor(hintColor)
        searchEditText.textSize = 13f

        binding.mainSearchView.setOnQueryTextFocusChangeListener { searchView, hasFocus ->
            if (hasFocus) {
                Log.d("searchview","아이템 클릭 ")

                val newFragment = SearchFragment() // 전환할 다른 프래그먼트 객체 생성
                val bundle = Bundle()
                newFragment.arguments = bundle

                // 프래그먼트 전환
                parentFragmentManager.beginTransaction()
                    .replace(R.id.home_layout, newFragment)
                    .addToBackStack(null)
                    .commit()

                binding.mainSearchView.clearFocus()
            }
        }
        return binding.root
    }


    private fun subscribeObservers() {
        viewModel.bannerItemList.observe(viewLifecycleOwner, Observer { bannerItemList ->
            bannerViewPagerAdapter.submitList(bannerItemList)
        })
    }
}