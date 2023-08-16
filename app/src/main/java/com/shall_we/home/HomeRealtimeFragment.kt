package com.shall_we.home

import android.graphics.Rect
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shall_we.ExperienceDetail.ExperienceDetailFragment
import com.shall_we.R
import com.shall_we.databinding.FragmentHomeRealtimeBinding
import com.shall_we.retrofit.RESPONSE_STATE
import com.shall_we.retrofit.RetrofitManager
import com.shall_we.utils.HorizontalSpaceItemDecoration
import com.shall_we.utils.dpToPx
import com.shall_we.utils.initProductRecycler


class HomeRealtimeFragment : Fragment(), ProductAdapter.OnItemClickListener, CategoryAdapter.OnItemClickListener{
    lateinit var textView : TextView
    lateinit var categoryAdapter: CategoryAdapter

    lateinit var rvRealtime : RecyclerView
    lateinit var rvCategory: RecyclerView

    val categoryData = mutableListOf<CategoryData>()



    override fun onItemClick(item: ProductData) {
        // 클릭된 아이템의 정보를 사용하여 다른 프래그먼트로 전환하는 로직을 작성
        val newFragment = ExperienceDetailFragment() // 전환할 다른 프래그먼트 객체 생성
        val bundle = Bundle()
        bundle.putString("title", item.title) // 클릭된 아이템의 이름을 "title" 키로 전달
        newFragment.arguments = bundle

        // 프래그먼트 전환
        parentFragmentManager.beginTransaction()
            .replace(R.id.home_layout, newFragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onItemClick(position: Int) {
        RetrofitCall(rvRealtime, position)
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentHomeRealtimeBinding.inflate(inflater,container,false)

        rvRealtime = binding.rvRealtime
        rvCategory = binding.rvCategory

        initCategoryRecycler(binding.rvCategory)

        RetrofitCall(rvRealtime, 1)

        // 1. TextView 참조
        textView = binding.realtimeText
        // 2. String 문자열 데이터 취득
        val textData: String = textView.text.toString()

        // 3. SpannableStringBuilder 타입으로 변환
        val builder = SpannableStringBuilder(textData)

        // 4 index=4에 해당하는 문자열(4)에 빨간색 적용
        val colorBlueSpan = ForegroundColorSpan(resources.getColor(R.color.rose600))
        builder.setSpan(colorBlueSpan, 4, 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        // 5. TextView에 적용
        textView.text = builder

        return binding.root
    }

    private fun initCategoryRecycler(rvCategory: RecyclerView) {
        categoryAdapter = CategoryAdapter(requireContext())
        categoryAdapter.setOnItemClickListener(this)

        rvCategory.adapter = categoryAdapter

        categoryData.apply {
            add(CategoryData(name = "전체"))
            add(CategoryData(name = "공예"))
            add(CategoryData(name = "베이킹"))
            add(CategoryData(name = "문화예술"))
            add(CategoryData(name = "아웃도어"))
            add(CategoryData(name = "스포츠"))

            categoryAdapter.datas = categoryData
            categoryAdapter.notifyDataSetChanged()
        }


        val spaceDecoration = HorizontalSpaceItemDecoration(dpToPx(7))
        rvCategory.addItemDecoration(spaceDecoration)

    }


    fun RetrofitCall(rv : RecyclerView, categoryId : Int){
        RetrofitManager.instance.experienceGiftExpCategory(categoryId = categoryId, category = "인기순", completion = {
                responseState, responseBody ->
            when(responseState){
                RESPONSE_STATE.OKAY -> {
                    Log.d("retrofit", "category api : ${responseBody?.size}")
                    initProductRecycler(rv, responseBody!!)
                }
                RESPONSE_STATE.FAIL -> {
                    Log.d("retrofit", "api 호출 에러")
                }
            }
        })
    }
}