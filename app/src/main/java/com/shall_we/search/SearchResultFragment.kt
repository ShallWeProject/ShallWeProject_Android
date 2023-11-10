package com.shall_we.search

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.shall_we.ExperienceDetail.ExperienceDetailFragment
import com.shall_we.R
import com.shall_we.base.BaseFragment
import com.shall_we.databinding.FragmentSearchResultBinding
import com.shall_we.home.ProductAdapter
import com.shall_we.home.ProductData
import com.shall_we.utils.initProductRecycler

class SearchResultFragment : BaseFragment<FragmentSearchResultBinding>(R.layout.fragment_search_result) , ProductAdapter.OnItemClickListener{
    lateinit var rv_search_result : RecyclerView
    lateinit var no_result : TextView

    override fun onItemClick(item: ProductData) {
        // 클릭된 아이템의 정보를 사용하여 다른 프래그먼트로 전환하는 로직을 작성
        val newFragment = ExperienceDetailFragment() // 전환할 다른 프래그먼트 객체 생성
        val bundle = Bundle()
        bundle.putInt("id", item.giftid)
        newFragment.arguments = bundle

        // 프래그먼트 전환
        parentFragmentManager.beginTransaction()
            .add(R.id.home_layout, newFragment)
            .addToBackStack(null)
            .commit()
    }

    override fun init() {
        rv_search_result = binding.rvSearchResult
        no_result = binding.noResult

        val status = arguments?.getBoolean("status",false)
        if (status == false){
            // 검색 결과 없으면
            // 리사이클러뷰 안보이게, 텍스트 보이게
            val query = arguments?.getString("query","")
            rv_search_result.visibility = View.GONE
            no_result.visibility = View.VISIBLE
            no_result.text = "\"${query}\"에 대한 검색 결과가 없습니다."
        } else{
            // 겸색 결과 있으면
            // 리사이클러뷰에 검색 결과 넣기, 텍스트는 안보이게
            val data = arguments?.getSerializable("data") as ArrayList<ProductData>
            rv_search_result.visibility = View.VISIBLE
            no_result.visibility = View.GONE
            initProductRecycler(rv_search_result, data,this)
        }
    }

}