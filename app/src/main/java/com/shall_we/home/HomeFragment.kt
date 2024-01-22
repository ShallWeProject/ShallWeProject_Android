package com.shall_we.home

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.shall_we.R
import com.shall_we.databinding.FragmentHomeBinding
import com.shall_we.search.SearchFragment


class HomeFragment : Fragment() {
    private lateinit var viewModel: HomeFragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.mainSearchView.clearFocus()

        val hintColor = Color.parseColor("#FEA3B4") // 원하는 힌트 색상으로 변경

        val searchEditText =
            binding.mainSearchView.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)

        searchEditText.setHintTextColor(hintColor)

        searchEditText.textSize = 13f

        binding.mainSearchView.setOnQueryTextFocusChangeListener { searchView, hasFocus ->
            if (hasFocus) {
                val newFragment = SearchFragment() // 전환할 다른 프래그먼트 객체 생성
                val bundle = Bundle()
                newFragment.arguments = bundle

                // 프래그먼트 전환
                parentFragmentManager.beginTransaction()
                    .add(R.id.home_layout, newFragment)
                    .addToBackStack(null)
                    .commit()
                binding.mainSearchView.clearFocus()

            }
        }

        viewModel = ViewModelProvider(this).get(HomeFragmentViewModel::class.java)
        viewModel.setBannerItems(
            listOf(
                BannerItem(R.drawable.banner_1),
                BannerItem(R.drawable.banner_2)
            )
        )

        // 어댑터 인스턴스 생성
        val homeBannerAdapter = HomeBannerAdapter(viewModel.bannerItemList.value ?: listOf())
        val homeRecomAdapter = HomeRecomAdapter()
        val homeRealtimeAdapter = HomeRealtimeAdapter()

        // ConcatAdapter에 어댑터들 추가
        val concatAdapter = ConcatAdapter()
        concatAdapter.addAdapter(0, homeBannerAdapter)
        concatAdapter.addAdapter(1, homeRecomAdapter)
        concatAdapter.addAdapter(2, homeRealtimeAdapter)

        binding.rvMain.layoutManager = LinearLayoutManager(requireContext())
        binding.rvMain.adapter = concatAdapter

        return binding.root
    }
}