package com.shall_we.search

import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shall_we.R
import com.shall_we.databinding.FragmentSearchHistoryBinding
import com.shall_we.home.dpToPx
import com.shall_we.utils.SharedPrefManager
import java.util.ArrayList
class SearchHistoryFragment : Fragment(), ISearchHistoryRecycler {
    lateinit var no_record : TextView
    lateinit var rv_search_result : RecyclerView
    lateinit var box_recent : ConstraintLayout


    lateinit var recentAdapter: RecentAdapter
    private var searchHistoryList = ArrayList<SearchData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentSearchHistoryBinding.inflate(inflater, container, false)

        this.no_record = binding.noRecord
        this.rv_search_result = binding.rvSearchResult
        this.box_recent = binding.boxRecent
        // 저장된 검색 기록 가져오기
        this.searchHistoryList = SharedPrefManager.getSearchHistoryList() as ArrayList<SearchData>

        // 검색 기록 있는지 확인
        handleSearchViewUi()

        // 검색기록 리사이클러뷰 준비
        initRecycler(binding.rvRecent, searchHistoryList)

        binding.deleteAll.setOnClickListener { // 전체 삭제 버튼 클릭
            SharedPrefManager.clearSearchHistoryList()
            this.searchHistoryList.clear()
            handleSearchViewUi()
        }
        return binding.root
    }

    fun initRecycler(rv: RecyclerView, searchHistoryList: ArrayList<SearchData>) {

        recentAdapter = RecentAdapter(requireContext(),this)
        recentAdapter.submitList(searchHistoryList)
        val myLinearLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, true)
        myLinearLayoutManager.stackFromEnd = true
        val spaceDecoration =SpaceItemDecoration(dpToPx(5),0)

        rv.apply {
            layoutManager = myLinearLayoutManager
            this.scrollToPosition(recentAdapter.itemCount -1) // 마지막 아이템으로 스크롤
            adapter = recentAdapter
            addItemDecoration(spaceDecoration)

        }

    }
    class SpaceItemDecoration(private val verticalSpaceWidth:Int, private val horizontalSpaceWidth:Int):RecyclerView.ItemDecoration(){
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            outRect.right = horizontalSpaceWidth
            outRect.top = verticalSpaceWidth
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

        this.insertSearchTermHistory(searchTerm = queryString)
        this.searchPhotoApiCall(queryString)

    }

    // 사진 검색 API 호출
    private fun searchPhotoApiCall(query: String){
     //레트로핏 연결
//        RetrofitManager.instance.searchPhotos(searchTerm = query, completion = { status, list ->
//            when(status){
//                RESPONSE_STATUS.OKAY -> {
//                    Log.d(TAG, "PhotoCollectionActivity - searchPhotoApiCall() called 응답 성공 / list.size : ${list?.size}")
//
//                    if (list != null){
//                        this.photoList.clear()
//                        this.photoList = list
//                        this.photoGridRecyeclerViewAdapter.submitList(this.photoList)
//                        this.photoGridRecyeclerViewAdapter.notifyDataSetChanged()
//                    }
//
//                }
//                RESPONSE_STATUS.NO_CONTENT -> {
//                    Toast.makeText(this, "$query 에 대한 검색 결과가 없습니다.", Toast.LENGTH_SHORT).show()
//                }
//            }
//        })

    }
    private fun handleSearchViewUi(){
        Log.d("history", "PhotoCollectionActivity - handleSearchViewUi() called / size : ${this.searchHistoryList.size}")


        if(this.searchHistoryList.size > 0){
            if (no_record != null) {
                no_record.visibility = View.GONE
            }
            if (rv_search_result != null) {
                rv_search_result.visibility = View.GONE
            }
            if (box_recent != null) {
                box_recent.visibility = View.VISIBLE
            }
        } else {
            if (no_record != null) {
                no_record.visibility = View.VISIBLE
            }
            if (rv_search_result != null) {
                rv_search_result.visibility = View.GONE
            }
            if (box_recent != null) {
                box_recent.visibility = View.GONE
            }
        }

    }

    // 검색어 저장
    private fun insertSearchTermHistory(searchTerm: String){
        Log.d("history", "insertSearchTermHistory() called")

        // 중복 아이템 삭제
        var indexListToRemove = ArrayList<Int>()

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