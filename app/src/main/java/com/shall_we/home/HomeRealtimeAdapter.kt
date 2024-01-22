package com.shall_we.home

import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.shall_we.R
import com.shall_we.databinding.FragmentHomeRealtimeBinding
import com.shall_we.retrofit.RESPONSE_STATE
import com.shall_we.retrofit.RetrofitManager
import com.shall_we.utils.HorizontalSpaceItemDecoration
import com.shall_we.utils.dpToPx
import com.shall_we.utils.initProductRecycler


class HomeRealtimeAdapter :
    RecyclerView.Adapter<HomeRealtimeAdapter.Holder>(),
    CategoryAdapter.OnItemClickListener {

    lateinit var rvRealtime: RecyclerView
    lateinit var rvCategory: RecyclerView
    lateinit var textView: TextView
    lateinit var categoryAdapter: CategoryAdapter

    val categoryData = mutableListOf<CategoryData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = FragmentHomeRealtimeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int {
        return 1
    }

    inner class Holder(var binding: FragmentHomeRealtimeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            rvRealtime = binding.rvRealtime
            rvCategory = binding.rvCategory

            initCategoryRecycler(binding.rvCategory)

            RetrofitCall(rvRealtime, 0)

            // 1. TextView 참조
            textView = binding.realtimeText
            // 2. String 문자열 데이터 취득
            val textData: String = textView.text.toString()

            // 3. SpannableStringBuilder 타입으로 변환
            val builder = SpannableStringBuilder(textData)

            // 4 index=4에 해당하는 문자열(4)에 빨간색 적용
            val colorBlueSpan = ForegroundColorSpan(ContextCompat.getColor(textView.context, R.color.rose600))
            builder.setSpan(colorBlueSpan, 4, 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

            // 5. TextView에 적용
            textView.text = builder

        }
    }
    private fun initCategoryRecycler(rvCategory: RecyclerView) {
        categoryAdapter = CategoryAdapter(rvCategory.context)
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

    override fun onItemClick(position: Int) {
        RetrofitCall(rvRealtime, position)
    }

    fun RetrofitCall(rv: RecyclerView, categoryId: Int) {
        if (categoryId == 0) {
            RetrofitManager.instance.experienceGiftPopular(completion = { responseState, responseBody ->
                when (responseState) {
                    RESPONSE_STATE.OKAY -> {
                        Log.d("retrofit", "popular api : ${responseBody?.size}")
                        val productDataList = ArrayList<ProductData>()
                        if (responseBody != null) {
                            for (experienceResNode in responseBody) {
                                val title: String = experienceResNode.title
                                val subtitle: String = experienceResNode.subtitle
                                val price: Int = experienceResNode.price
                                val formattedPrice = String.format("%,d", price.toInt())
                                val giftImgUrl: String
                                if (experienceResNode.giftImgUrl.size == 0) {
                                    giftImgUrl = ""
                                } else {
                                    giftImgUrl = experienceResNode.giftImgUrl[0]
                                }
                                val giftid: Int = experienceResNode.experienceGiftId
                                // ProductData 객체를 ArrayList에 추가
                                productDataList.add(
                                    ProductData(
                                        title = title,
                                        subtitle = subtitle,
                                        price = formattedPrice,
                                        img = giftImgUrl,
                                        giftid = giftid
                                    )
                                )
                            }
                        }
                        initProductRecycler(rv, productDataList)
                    }

                    RESPONSE_STATE.FAIL -> {
                        Log.d("retrofit", "api 호출 에러")
                    }
                }
            })
        } else {
            RetrofitManager.instance.experienceGiftExpCategory(
                categoryId = categoryId,
                category = "인기순",
                completion = { responseState, responseBody ->
                    when (responseState) {
                        RESPONSE_STATE.OKAY -> {
                            Log.d("retrofit", "category api : ${responseBody?.size}")
                            val productDataList = ArrayList<ProductData>()
                            if (responseBody != null) {
                                for (experienceResNode in responseBody) {
                                    val title: String = experienceResNode.title
                                    val subtitle: String = experienceResNode.subtitle
                                    val price: Int = experienceResNode.price
                                    val formattedPrice = String.format("%,d", price.toInt())
                                    val giftImgUrl: String
                                    if (experienceResNode.giftImgUrl.size == 0) {
                                        giftImgUrl = ""
                                    } else {
                                        giftImgUrl = experienceResNode.giftImgUrl[0]
                                    }
                                    val giftid: Int = experienceResNode.experienceGiftId
                                    // ProductData 객체를 ArrayList에 추가
                                    productDataList.add(
                                        ProductData(
                                            title = title,
                                            subtitle = subtitle,
                                            price = formattedPrice,
                                            img = giftImgUrl,
                                            giftid = giftid
                                        )
                                    )
                                }
                            }
                            initProductRecycler(rv, productDataList)
                        }

                        RESPONSE_STATE.FAIL -> {
                            Log.d("retrofit", "api 호출 에러")
                        }
                    }
                }
            )
        }
    }
}
