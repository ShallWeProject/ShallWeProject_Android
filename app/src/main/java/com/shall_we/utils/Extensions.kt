package com.shall_we.utils

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shall_we.home.ProductAdapter
import com.shall_we.home.ProductData

// 문자열이 제이슨 형태인지
fun String?.isJsonObject():Boolean {
    return this?.startsWith("{") == true && this.endsWith("}")
}

// 문자열이 제이슨 베열인지
fun String?.isJsonArray() : Boolean {
    return this?.startsWith("{") == true && this.endsWith("}")
}

fun initProductRecycler(rv: RecyclerView, resultData: ArrayList<ProductData>) {
    val resultAdapter = ProductAdapter(rv.context)
    val layoutManager = GridLayoutManager(rv.context, 2, GridLayoutManager.VERTICAL, false)
    layoutManager.recycleChildrenOnDetach = true
    rv.layoutManager = layoutManager
    rv.adapter = resultAdapter
    resultAdapter.datas = resultData
    resultAdapter.notifyDataSetChanged()
}