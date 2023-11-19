package com.shall_we.home

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.shall_we.ExperienceDetail.ExperienceDetailFragment
import com.shall_we.R
import com.shall_we.databinding.FragmentHomeBinding
import com.shall_we.dto.ExperienceRes
import com.shall_we.retrofit.RESPONSE_STATE
import com.shall_we.retrofit.RetrofitManager
import com.shall_we.search.SearchFragment
import com.shall_we.utils.HorizontalSpaceItemDecoration
import com.shall_we.utils.dpToPx
import com.shall_we.utils.initProductRecycler


class HomeFragment : Fragment() , ProductAdapter.OnItemClickListener, CategoryAdapter.OnItemClickListener {
    private lateinit var bannerViewPagerAdapter: BannerViewPagerAdapter
    private lateinit var viewModel: HomeFragmentViewModel
    private lateinit var pager: ViewPager2
    var currentPosition = 0

    lateinit var textView: TextView
    lateinit var categoryAdapter: CategoryAdapter

    lateinit var rvRealtime: RecyclerView
    lateinit var rvCategory: RecyclerView

    val categoryData = mutableListOf<CategoryData>()


    override fun onItemClick(item: ProductData) {
        // 클릭된 아이템의 정보를 사용하여 다른 프래그먼트로 전환하는 로직을 작성
        val newFragment = ExperienceDetailFragment() // 전환할 다른 프래그먼트 객체 생성
        val bundle = Bundle()
        bundle.putInt("id", item.giftid) // 클릭된 아이템의 이름을 "title" 키로 전달
        newFragment.arguments = bundle
        // 프래그먼트 전환
        parentFragmentManager.beginTransaction()
            .add(R.id.home_layout, newFragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onItemClick(position: Int) {
        RetrofitCall(rvRealtime, position)
    }

    val handler = Handler(Looper.getMainLooper()) {
        setPage()
        true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentHomeBinding.inflate(inflater, container, false)

        pager = binding.viewPager2

        viewModel = ViewModelProvider(this).get(HomeFragmentViewModel::class.java)
        viewModel.setBannerItems(
            listOf(
                BannerItem(R.drawable.banner_1),
                BannerItem(R.drawable.banner_2)
            )
        )
        pager.apply {
            bannerViewPagerAdapter = BannerViewPagerAdapter()
            adapter = bannerViewPagerAdapter
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    binding.tvPageNumber.text = "${position + 1} | 2"
                }
            })
        }
        subscribeObservers()

        //뷰페이저 넘기는 쓰레드
        val thread = Thread(PagerRunnable())
        thread.start()
        binding.mainSearchView.clearFocus()
        val hintColor = Color.parseColor("#FEA3B4") // 원하는 힌트 색상으로 변경
        val searchEditText =
            binding.mainSearchView.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)
        searchEditText.setHintTextColor(hintColor)
        searchEditText.textSize = 13f

        binding.mainSearchView.setOnQueryTextFocusChangeListener { searchView, hasFocus ->
            if (hasFocus) {
                val newFragment = SearchFragment() // 전환할 다른 프래그먼트 객체 생성
                val bundle = Bundle()
                newFragment.arguments = bundle

                // 프래그먼트 전환
                parentFragmentManager.beginTransaction()
                    .add(R.id.home_layout, newFragment)
                    .addToBackStack(null)
                    .commit()
                binding.mainSearchView.clearFocus()

            }
        }

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
        val colorBlueSpan = ForegroundColorSpan(resources.getColor(R.color.rose600))
        builder.setSpan(colorBlueSpan, 4, 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        // 5. TextView에 적용
        textView.text = builder

        return binding.root
    }

    //페이지 변경하기
    fun setPage() {
        if (currentPosition == 2) currentPosition = 0
        pager.setCurrentItem(currentPosition, true)
        currentPosition += 1
    }

    //2초 마다 페이지 넘기기
    inner class PagerRunnable : Runnable {
        override fun run() {
            while (true) {
                Thread.sleep(5000)
                handler.sendEmptyMessage(0)
            }
        }
    }

    private fun subscribeObservers() {
        viewModel.bannerItemList.observe(viewLifecycleOwner, Observer { bannerItemList ->
            bannerViewPagerAdapter.submitList(bannerItemList)
        })
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

        if(categoryId == 0){

            RetrofitManager.instance.experienceGiftPopular(completion = { responseState, responseBody ->
                when (responseState) {
                    RESPONSE_STATE.OKAY -> {
                        Log.d("retrofit", "popular api : ${responseBody?.size}")
                        val productDataList = ArrayList<ProductData>()
                        if (responseBody != null) {
                            for (experienceResNode in responseBody) {
                                val title: String = experienceResNode.title ?: ""
                                val subtitle: String = experienceResNode.subtitle ?: ""
                                val price: Int = experienceResNode.price ?: 0
                                val formattedPrice = String.format("%,d", price.toInt())
                                val giftImgUrl: String = experienceResNode.giftImgUrl ?: ""
                                val giftid: Int = experienceResNode.experienceGiftId ?: 0
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
                        initProductRecycler(rv, productDataList, this)
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
                                    val title: String = experienceResNode.title ?: ""
                                    val subtitle: String = experienceResNode.subtitle ?: ""
                                    val price: Int = experienceResNode.price ?: 0
                                    val formattedPrice = String.format("%,d", price.toInt())
                                    val giftImgUrl: String = experienceResNode.giftImgUrl ?: ""
                                    val giftid: Int = experienceResNode.experienceGiftId ?: 0
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
                            initProductRecycler(rv, productDataList, this)
                        }
                        RESPONSE_STATE.FAIL -> {
                            Log.d("retrofit", "api 호출 에러")
                        }
                    }
                })
            //}

        }
    }
}