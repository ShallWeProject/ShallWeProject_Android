package com.shall_we.search

import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.shall_we.R
import com.shall_we.base.BaseFragment
import com.shall_we.databinding.FragmentSearchResultBinding
import com.shall_we.home.ProductData
import com.shall_we.retrofit.RESPONSE_STATE
import com.shall_we.retrofit.RetrofitManager
import com.shall_we.utils.initProductRecycler
import retrofit2.Retrofit

class SearchResultFragment : BaseFragment<FragmentSearchResultBinding>(R.layout.fragment_search_result) {
    lateinit var rv_search_result : RecyclerView
    lateinit var no_result : TextView


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
            initProductRecycler(rv_search_result, data)
        }
    }

}