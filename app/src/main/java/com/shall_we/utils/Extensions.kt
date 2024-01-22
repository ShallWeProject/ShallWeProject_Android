package com.shall_we.utils

import android.content.res.Resources
import android.graphics.Rect
import android.util.TypedValue
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shall_we.dto.ExperienceRes
import com.shall_we.home.ProductAdapter
import com.shall_we.home.ProductData
import com.shall_we.search.SearchResultFragment

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
//    resultAdapter.setOnItemClickListener(itemClickListener)
    layoutManager.recycleChildrenOnDetach = true
    rv.layoutManager = layoutManager
    rv.adapter = resultAdapter
    resultAdapter.datas = resultData
    resultAdapter.notifyDataSetChanged()
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

class HorizontalSpaceItemDecoration(private val horizontalSpaceWidth:Int):RecyclerView.ItemDecoration(){
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.right = horizontalSpaceWidth
    }
}

fun dpToPx(dp: Int) : Int{
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp.toFloat(),
        Resources.getSystem().displayMetrics).toInt()

}