package com.shall_we.utils

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.shall_we.App
import com.shall_we.search.SearchData

object SharedPrefManager {
    private const val SHARED_SEARCH_HISTORY = "shared_search_history"
    private const val KEY_SEARCH_HISTORY = "key_search_history"


    // 검색 목록 저장
    fun storeSearchHistoryList(searchHistoryList: MutableList<SearchData>){
        Log.d("save","SharedPrefManager")

        // 매개 변수로 들어온 배열을 문자열로 변환
        val searchHistoryListString : String = Gson().toJson(searchHistoryList)
        Log.d("save","SharedPrefManager - $searchHistoryList")

        //쉐어드 가져오기
        val shared = App.instance.getSharedPreferences(SHARED_SEARCH_HISTORY, Context.MODE_PRIVATE)

        //쉐어드 에디터 가져오기
        val editor = shared.edit()

        editor.putString(KEY_SEARCH_HISTORY,searchHistoryListString)

        editor.apply() // 변경사항 적용
    }

    // 검색 목록 가져오기
    fun getSearchHistoryList() : MutableList<SearchData>{
        //쉐어드 가져오기
        val appInstance = App.instance

        val shared = appInstance.getSharedPreferences(SHARED_SEARCH_HISTORY, Context.MODE_PRIVATE)

        val storedSearchHistoryListString = shared.getString(KEY_SEARCH_HISTORY, "")!!

        var storedSearchHistoryList = ArrayList<SearchData>()
        // 검색 목록이 있다면
        if (storedSearchHistoryListString.isNotEmpty()){
            storedSearchHistoryList = Gson().fromJson(storedSearchHistoryListString, Array<SearchData>::class.java)
                .toMutableList() as ArrayList<SearchData>
        }
        return storedSearchHistoryList
    }

    // 검색 목록 지우기
    fun clearSearchHistoryList(){
        //쉐어드 가져오기
        val shared = App.instance.getSharedPreferences(SHARED_SEARCH_HISTORY, Context.MODE_PRIVATE)

        //쉐어드 에디터 가져오기
        val editor = shared.edit()

        editor.clear() // 지우기

        editor.apply() // 변경사항 적용
    }
}