package com.shall_we.search

import android.annotation.SuppressLint
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shall_we.R
import com.shall_we.databinding.FragmentSearchHistoryBinding
import com.shall_we.home.ProductAdapter
import com.shall_we.home.ProductData
import com.shall_we.home.dpToPx
import com.shall_we.retrofit.RESPONSE_STATE
import com.shall_we.retrofit.RetrofitManager
import com.shall_we.utils.SharedPrefManager
import com.shall_we.utils.initProductRecycler
import java.util.ArrayList
class SearchHistoryFragment : Fragment(), ISearchHistoryRecycler {
    lateinit var no_record : TextView
    lateinit var rv_search_result : RecyclerView
    lateinit var box_recent : ConstraintLayout
    lateinit var no_result : TextView

    lateinit var recentAdapter: RecentAdapter
    private var searchHistoryList = ArrayList<SearchData>()

    lateinit var resultAdapter: ProductAdapter
    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
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
        this.no_result = binding.noResult
        //뷰 초기 세팅
        rv_search_result.visibility = View.GONE
        no_result.visibility = View.GONE

        // 저장된 검색 기록 가져오기
        this.searchHistoryList = SharedPrefManager.getSearchHistoryList() as ArrayList<SearchData>

        // 검색 기록 있는지 확인
        handleSearchViewUi()

        // 검색기록 리사이클러뷰 준비
        initHistoryRecycler(binding.rvRecent, searchHistoryList)

        binding.deleteAll.setOnClickListener { // 전체 삭제 버튼 클릭
            SharedPrefManager.clearSearchHistoryList()
            this.searchHistoryList.clear()
            handleSearchViewUi()
        }
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 데이터 변경을 관찰하여 업데이트
        sharedViewModel.data.observe(viewLifecycleOwner) { data ->
            // 데이터를 사용하여 작업 수행
            searchResultCall(data)
        }
    }
    fun initHistoryRecycler(rv: RecyclerView, searchHistoryList: ArrayList<SearchData>) {

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

        searchResultCall(queryString)
    }

    @SuppressLint("SetTextI18n")
    fun searchResultCall(queryString : String) {
        // 히스토리 관련 뷰들 안보이게 설정
        no_record = requireActivity().findViewById(R.id.no_record)
        box_recent = requireActivity().findViewById(R.id.box_recent)
        rv_search_result = requireActivity().findViewById(R.id.rv_search_result)
        no_result = requireActivity().findViewById(R.id.no_result)

        no_record.visibility = View.GONE
        box_recent.visibility = View.GONE

        // 검색 API 불러오기
        this.searchPhotoApiCall(queryString)
    }

    // 사진 검색 API 호출
    private fun searchPhotoApiCall(query: String){
     //레트로핏 연결
        RetrofitManager.instance.experienceGiftSearch(title = query, completion = { responseState, responseBody ->
            when (responseState) {
                RESPONSE_STATE.OKAY -> {
                    Log.d("retrofit", "api 호출 성공1 : ${responseBody!!}")
                    if (responseBody.size != 0) {
                        // 겸색 결과 있으면
                        // 리사이클러뷰에 검색 결과 넣기, 텍스트는 안보이게
                        rv_search_result.visibility = View.VISIBLE
                        no_result.visibility = View.GONE
                        initProductRecycler(rv_search_result, responseBody)

                    }else{
                        // 검색 결과 없으면
                        // 리사이클러뷰 안보이게, 텍스트 보이게
                        rv_search_result.visibility = View.GONE
                        no_result.visibility = View.VISIBLE
                        no_result.text = "\"${query}\"에 대한 검색 결과가 없습니다."
                    }
                }

                RESPONSE_STATE.FAIL -> {
                    Log.d("retrofit", "api 호출 에러")
                }
            }
        })

    }
    private fun handleSearchViewUi(){
        Log.d("history", "PhotoCollectionActivity - handleSearchViewUi() called / size : ${this.searchHistoryList.size}")


        if(this.searchHistoryList.size > 0){
            if (no_record != null) {
                no_record.visibility = View.GONE
            }
            if (box_recent != null) {
                box_recent.visibility = View.VISIBLE
            }
        } else {
            if (no_record != null) {
                no_record.visibility = View.VISIBLE
            }
            if (box_recent != null) {
                box_recent.visibility = View.GONE
            }
        }

    }

    private fun handleResultViewUi(queryString : String){
        // 검색 API 불러오기
        this.searchPhotoApiCall(queryString)

        // 겸색 결과 있으면

        // 리사이클러뷰에 검색 결과 넣기, 텍스트는 안보이게
//        rv_search_result.visibility = View.VISIBLE
//        no_result.visibility = View.GONE
        // 검색 결과 없으면
        // 리사이클러뷰 안보이게, 텍스트 보이게
        rv_search_result.visibility = View.GONE
        no_result.visibility = View.VISIBLE
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