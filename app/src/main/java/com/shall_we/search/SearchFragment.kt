package com.shall_we.search

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.shall_we.R
import com.shall_we.databinding.FragmentSearchBinding
import com.shall_we.home.ProductData
import com.shall_we.retrofit.RESPONSE_STATE
import com.shall_we.retrofit.RetrofitManager
import com.shall_we.utils.SharedPrefManager

class SearchFragment : Fragment() , SearchView.OnQueryTextListener,
    SearchHistoryFragment.OnSearchHistoryClickListener {
    private var searchView = view?.findViewById<SearchView>(R.id.searchView)
    private lateinit var searchHistoryFragment: SearchHistoryFragment

    //검색 기록 배열
    private var searchHistoryList = ArrayList<SearchData>()


    private lateinit var fragmentBox: FragmentContainerView

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
        val binding = FragmentSearchBinding.inflate(inflater,container,false)

        this.fragmentBox = binding.fragmentBox

        //searchview 세팅
        searchView = binding.searchView



        this.searchView?.apply {
            requestFocus()
            setOnQueryTextListener(this@SearchFragment)
        }

        val hintColor = Color.parseColor("#FEA3B4") // 원하는 힌트 색상으로 변경
        val textColor = Color.parseColor("#333333") // 원하는 텍스트 색상으로 변경

        val searchEditText =
            searchView!!.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)

        searchEditText.apply {
            setHintTextColor(hintColor)
            setTextColor(textColor)
            inputType = InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS or InputType.TYPE_TEXT_FLAG_MULTI_LINE
            textSize = 13f
        }

        this.searchView!!.setOnQueryTextFocusChangeListener { searchView, hasFocus ->
            if (hasFocus) {
                // focus 를 가지고 있는 경우 - show keyboard
                val imm: InputMethodManager = getContext()?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_IMPLICIT_ONLY)

            }
        }

        searchHistoryFragment = childFragmentManager.findFragmentById(R.id.fragmentBox) as SearchHistoryFragment
        searchHistoryFragment.setOnSearchHistoryClickListener(this@SearchFragment)

        return binding.root
    }

    //서치뷰 검색어 입력 이벤트
    override fun onQueryTextSubmit(query: String?): Boolean {
        // 검색 버튼 누를 때 호출
        this.searchView?.setQuery(query,false)
        this.searchView?.clearFocus()

        // TODO:: api 호출
        // TODO:: 검색어 저장
        val newSearchData = SearchData(search_word = query.toString())

        this.searchHistoryList = SharedPrefManager.getSearchHistoryList() as ArrayList<SearchData>

        this.searchHistoryList.add(newSearchData)

        SharedPrefManager.storeSearchHistoryList(this.searchHistoryList)

        insertSearchTermHistory(query.toString())
        searchPhotoApiCall(query.toString())


        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        // 검색창에서 글자가 변경이 일어날 때마다 호출

        val userInputText = newText ?: ""

        return true
    }

        // 검색 API 호출
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
                        .replace(R.id.fragmentBox, newFragment)
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

        // 뒤에서부터 제거
        for (i in indexListToRemove.size - 1 downTo 0) {
            this.searchHistoryList.removeAt(indexListToRemove[i])
        }

        // 새 아이템 넣기
        val newSearchData = SearchData(search_word = searchTerm)
        this.searchHistoryList.add(newSearchData)

        // 기존 데이터에 덮어쓰기
        SharedPrefManager.storeSearchHistoryList(this.searchHistoryList)
    }

    override fun onSearchHistoryClicked(query: String) {
        Log.d("history","$query")
        this.searchView?.setQuery(query, true)
    }
}
class SharedViewModel : ViewModel() {
    val data: MutableLiveData<String> = MutableLiveData()
}