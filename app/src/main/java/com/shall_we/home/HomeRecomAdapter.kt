package com.shall_we.home

import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.shall_we.R
import com.shall_we.databinding.FragmentHomeRecomBinding
import com.shall_we.utils.HorizontalSpaceItemDecoration
import com.shall_we.utils.dpToPx


class HomeRecomAdapter:
    RecyclerView.Adapter<HomeRecomAdapter.Holder>() {
    lateinit var textView : TextView
    lateinit var recomAdapter: RecomAdapter
    val recomData = mutableListOf<RecomData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = FragmentHomeRecomBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int {
        return 1
    }

    inner class Holder(var binding: FragmentHomeRecomBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            initRecycler(binding.rvRecom)

            // 1. TextView 참조
            textView = binding.recomText
            // 2. String 문자열 데이터 취득
            val textData: String = textView.text.toString()

            // 3. SpannableStringBuilder 타입으로 변환
            val builder = SpannableStringBuilder(textData)

            // 4 index=4에 해당하는 문자열(4)에 빨간색 적용
            val colorBlueSpan = ForegroundColorSpan(ContextCompat.getColor(textView.context,R.color.rose600))
            builder.setSpan(colorBlueSpan, 0, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

            // 5. TextView에 적용
            textView.text = builder
        }
    }
    private fun initRecycler(rvCategory: RecyclerView) {
        recomAdapter = RecomAdapter(rvCategory.context)
        rvCategory.adapter = recomAdapter

        recomData.apply {
            add(RecomData(img = R.drawable.recom_birth, name = "생일"))
            add(RecomData(img = R.drawable.recom_couple, name = "연인"))
            add(RecomData(img = R.drawable.recom_parent, name = "부모님"))
            add(RecomData(img = R.drawable.recom_school, name = "입학/졸업"))
            add(RecomData(img = R.drawable.recom_merry, name = "결혼/집들이"))

            recomAdapter.datas = recomData
            recomAdapter.notifyDataSetChanged()
        }
        val spaceDecoration = HorizontalSpaceItemDecoration(dpToPx(7))
        rvCategory.addItemDecoration(spaceDecoration)
    }
}

data class RecomData (
    val img : Int,
    val name : String,
)