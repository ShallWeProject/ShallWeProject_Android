package com.shall_we.search

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shall_we.R
import com.shall_we.base.BaseFragment
import com.shall_we.databinding.FragmentSearchHistoryBinding
import com.shall_we.retrofit.RESPONSE_STATE
import com.shall_we.retrofit.RetrofitManager
import com.shall_we.utils.SharedPrefManager
import com.shall_we.utils.SpaceItemDecoration
import com.shall_we.utils.dpToPx

class SearchHistoryFragment : BaseFragment<FragmentSearchHistoryBinding>(R.layout.fragment_search_history), ISearchHistoryRecycler {
    lateinit var box_recent : ConstraintLayout
    lateinit var no_history : TextView

    private lateinit var listener: OnSearchHistoryClickListener

    interface OnSearchHistoryClickListener {
        fun onSearchHistoryClicked(query: String)
    }

    fun setOnSearchHistoryClickListener(listener: OnSearchHistoryClickListener) {
        this.listener = listener
    }

    // 검색 기록 배열
    private var searchHistoryList = ArrayList<SearchData>()

    // 최근 검색어 어댑터
    lateinit var recentAdapter: RecentAdapter

    private lateinit var sharedViewModel: SharedViewModel

    override fun init() {

        box_recent = binding.boxRecent
        no_history = binding.noHistory

        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        //저장된 검색 기록 가져오기
        this.searchHistoryList = SharedPrefManager.getSearchHistoryList() as ArrayList<SearchData>

        // 검색 기록 있는지 확인
        handleSearchViewUi()

        // 전체 삭제 버튼 클릭
        binding.deleteAll.setOnClickListener {
            SharedPrefManager.clearSearchHistoryList()
            this.searchHistoryList.clear()
            SharedPrefManager.storeSearchHistoryList(this.searchHistoryList)
            handleSearchViewUi()
        }

    }

    private fun handleSearchViewUi(){
        if(this.searchHistoryList.size > 0){
            no_history.visibility = View.GONE
            box_recent.visibility = View.VISIBLE
            // 검색기록 리사이클러뷰 준비
            initHistoryRecycler(binding.rvRecent, searchHistoryList)

        } else {
            no_history.visibility = View.VISIBLE
            box_recent.visibility = View.GONE
        }
    }

    fun initHistoryRecycler(rv: RecyclerView, searchHistoryList: java.util.ArrayList<SearchData>) {
        recentAdapter = RecentAdapter(requireContext(),this)
        recentAdapter.submitList(searchHistoryList)
        val myLinearLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, true)
        myLinearLayoutManager.stackFromEnd = true
        val spaceDecoration = SpaceItemDecoration(dpToPx(5), 0)

        rv.apply {
            layoutManager = myLinearLayoutManager
            this.scrollToPosition(recentAdapter.itemCount - 1) // 마지막 아이템으로 스크롤
            adapter = recentAdapter
            addItemDecoration(spaceDecoration)
        }
    }

    // 검색 아이템 삭제 버튼 이벤트
    override fun onSearchItemDeleteClicked(position: Int) {
        Log.d("history","아이템 삭제 $position")
        // 해당 아이템 삭제
        this.searchHistoryList.removeAt(position)
        // 다시 저장(데이터 덮어 쓰기)
        SharedPrefManager.storeSearchHistoryList(this.searchHistoryList)
        // 데이터 변경 됐다고 알려줌
        this.recentAdapter.notifyDataSetChanged()

        handleSearchViewUi()


    }

    // 검색 아이템 버튼 이벤트
    override fun onSearchItemClicked(position: Int) {
        Log.d("history","아이템 클릭 $position")

        // 해당 검색어 api 호출
        val queryString = this.searchHistoryList[position].search_word
        Log.d("history","아이템 클릭 $queryString")
        listener.onSearchHistoryClicked(queryString)
    }
    private fun searchPhotoApiCall(query: String){
        //레트로핏 연결
        RetrofitManager.instance.experienceGiftSearch(title = query, completion = { responseState, responseBody ->
            when (responseState) {
                RESPONSE_STATE.OKAY -> {
                    Log.d("retrofit", "api 호출 성공1 : ${responseBody!!}")
                    // 클릭된 아이템의 정보를 사용하여 다른 프래그먼트로 전환하는 로직을 작성
                    val newFragment = SearchResultFragment() // 전환할 다른 프래그먼트 객체 생성
                    val bundle = Bundle()
                    if(responseBody.size == 0){
                        bundle.putBoolean("status",false)
                        bundle.putString("query",query)
                    }
                    else{
                        bundle.putBoolean("status", true)
                        bundle.putSerializable("data", responseBody)
                    }
                    newFragment.arguments = bundle

                    // 프래그먼트 전환
                    parentFragmentManager.beginTransaction()
                        .add(R.id.fragmentBox, newFragment)
                        .addToBackStack(null)
                        .commit()

                }
                RESPONSE_STATE.FAIL -> {
                    Log.d("retrofit", "api 호출 에러")
                }
            }
        })

    }

    // 검색어 저장
    @SuppressLint("NotifyDataSetChanged")
    private fun insertSearchTermHistory(searchTerm: String){
        Log.d("history", "insertSearchTermHistory() called")

        // 중복 아이템 삭제
        var indexListToRemove = java.util.ArrayList<Int>()

        this.searchHistoryList.forEachIndexed{ index, searchDataItem ->

            if(searchDataItem.search_word == searchTerm){
                Log.d("history", "index: $index")
                indexListToRemove.add(index)
            }
        }

        indexListToRemove.forEach {
            this.searchHistoryList.removeAt(it)
        }

        // 새 아이템 넣기
        val newSearchData = SearchData(search_word = searchTerm)
        this.searchHistoryList.add(newSearchData)

        // 기존 데이터에 덮어쓰기
        SharedPrefManager.storeSearchHistoryList(this.searchHistoryList)

        this.recentAdapter.notifyDataSetChanged()

    }

}