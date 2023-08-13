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
import com.shall_we.ExperienceDetail.ExDetailFragment
import com.shall_we.ExperienceDetail.ExperienceDetailFragment
import com.shall_we.R
import com.shall_we.databinding.FragmentHomeRealtimeBinding
import com.shall_we.retrofit.RESPONSE_STATE
import com.shall_we.retrofit.RetrofitManager


class HomeRealtimeFragment : Fragment(), ProductAdapter.OnItemClickListener {
    lateinit var textView : TextView
    lateinit var productAdapter: ProductAdapter
    lateinit var categoryAdapter: CategoryAdapter

    val productData = mutableListOf<ProductData>()
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentHomeRealtimeBinding.inflate(inflater,container,false)
        RetrofitManager.instace.experienceGiftSttCategory(categoryId = 1, category = "가격높은순", completion = { // 카테고리별 경험으로 바꾸기
                responseState, responseBody ->
            when(responseState){
                RESPONSE_STATE.OKAY -> {
                    Log.d("retrofit", "api 호출 성공1 : ${responseBody?.size}")
                    initRecycler(binding.rvRealtime, binding.rvCategory, responseBody!!)


                }
                RESPONSE_STATE.FAIL -> {
                    Log.d("retrofit", "api 호출 에러")
                }
            }
        })


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

    private fun initRecycler(rvRealtime: RecyclerView,rvCategory: RecyclerView, data : ArrayList<ProductData>) {
        productAdapter = ProductAdapter(requireContext())
        productAdapter.setOnItemClickListener(this)
        val layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
        rvRealtime.layoutManager = layoutManager
        rvRealtime.adapter = productAdapter

        productAdapter.datas = data
        productAdapter.notifyDataSetChanged()

//            rvRealtime.addItemDecoration(GridSpaceItemDecoration(2, dpToPx(8)))

        categoryAdapter = CategoryAdapter(requireContext())
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

        val spaceDecoration = HomeRecomFragment.HorizontalSpaceItemDecoration(dpToPx(7))
        rvCategory.addItemDecoration(spaceDecoration)

    }


    class GridSpaceItemDecoration(private val spanCount: Int, private val space: Int): RecyclerView.ItemDecoration() {

        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            val position = parent.getChildAdapterPosition(view)
            val column = position % spanCount + 1      // 1부터 시작

            /** 첫번째 행(row-1) 이후부터 있는 아이템에만 상단에 [space] 만큼의 여백을 추가한다. 즉, 첫번째 행에 있는 아이템에는 상단에 여백을 주지 않는다.*/
            if (position >= spanCount){
                outRect.top = space
            }
            /** 첫번째 열이 아닌(None Column-1) 아이템들만 좌측에 [space] 만큼의 여백을 추가한다. 즉, 첫번째 열에 있는 아이템에는 좌측에 여백을 주지 않는다. */
            if (column != 1){
                outRect.left = space
            }

        }

    }



}