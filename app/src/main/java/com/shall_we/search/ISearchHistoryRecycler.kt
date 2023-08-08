package com.shall_we.search

interface ISearchHistoryRecycler {
    // 검색 아이템 삭제 버튼 클릭
    fun onSearchItemDeleteClicked(position: Int)

    // 검색 버튼 클릭
    fun onSearchItemClicked(position: Int)
}