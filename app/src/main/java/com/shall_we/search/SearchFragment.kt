package com.shall_we.search

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
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import com.shall_we.R
import com.shall_we.databinding.FragmentSearchBinding
import com.shall_we.utils.SharedPrefManager

class SearchFragment : Fragment() , SearchView.OnQueryTextListener{
    private var searchView = view?.findViewById<SearchView>(R.id.searchView)
    //검색 기록 배열
    private var searchHistoryList = ArrayList<SearchData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentSearchBinding.inflate(inflater,container,false)
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

        //저장된 검색 기록 가져오기
        this.searchHistoryList = SharedPrefManager.getSearchHistoryList() as ArrayList<SearchData>
        this.searchHistoryList.forEach{
            Log.d("history", it.search_word)
        }
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

        this.searchHistoryList.add(newSearchData)

        SharedPrefManager.storeSearchHistoryList(this.searchHistoryList)

        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        // 검색창에서 글자가 변경이 일어날 때마다 호출

        val userInputText = newText ?: ""

        return true
    }


}