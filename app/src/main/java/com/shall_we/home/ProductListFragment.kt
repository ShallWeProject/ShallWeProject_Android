package com.shall_we.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.LinearLayout
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.shall_we.R
import com.shall_we.databinding.FragmentProductListBinding
import com.shall_we.retrofit.RESPONSE_STATE
import com.shall_we.retrofit.RetrofitManager
import com.shall_we.utils.initProductRecycler


class ProductListFragment : Fragment() {
    var category : Boolean = true
    var spinnerString : String = "인기순"
    var tabPosition : Int = 0
    lateinit var rvProduct : RecyclerView

    override fun onResume() {
        super.onResume()
        val supportActionBar = (requireActivity() as AppCompatActivity).supportActionBar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentProductListBinding.inflate(inflater,container,false)
        val tab = arguments?.getString("tab", "경험카테고리")
        tabPosition = arguments?.getInt("position", 0)!!
        this.rvProduct = binding.rvProduct

        //tabbar 설정
        val tabLayout: TabLayout = binding.tabLayout
        val tabCount: Int = tabLayout.tabCount

        if(tab == "경험카테고리"){
            val tabArray = arrayOf("공예","베이킹","문화예술","아웃도어","스포츠")
            category = false
            for (i in 0 until tabLayout.tabCount) {
                val tab = tabLayout.getTabAt(i)
                tab?.text = tabArray[i].toString()
            }
            for (i in 0 until tabCount) {
                val tab: TabLayout.Tab? = tabLayout.getTabAt(i)
                tab?.let {
                    val tabLayoutParams: LinearLayout.LayoutParams =
                        tab.view.layoutParams as LinearLayout.LayoutParams
                    when (i) { // 탭바 너비 글자수에 맞춰서
                        0, 1 -> tabLayoutParams.weight = 36f
                        2 -> tabLayoutParams.weight = 49f
                        3 -> tabLayoutParams.weight = 49f
                        else -> tabLayoutParams.weight = 36f
                    }
                    tab.view.layoutParams = tabLayoutParams
                }
            }
        }
        else {
            val tabArray = arrayOf("생일","연인","부모님","입학/졸업","결혼/집들이")
            category = true
            for (i in 0 until tabLayout.tabCount) {
                val tab = tabLayout.getTabAt(i)
                tab?.text = tabArray[i].toString()
            }
            for (i in 0 until tabCount) {
                val tab: TabLayout.Tab? = tabLayout.getTabAt(i)
                tab?.let {
                    val tabLayoutParams: LinearLayout.LayoutParams =
                        tab.view.layoutParams as LinearLayout.LayoutParams
                    when (i) { // 탭바 너비 글자수에 맞춰서
                        0, 1 -> tabLayoutParams.weight = 36f
                        2 -> tabLayoutParams.weight = 49f
                        3 -> tabLayoutParams.weight = 67f
                        else -> tabLayoutParams.weight = 80f
                    }
                    tab.view.layoutParams = tabLayoutParams
                }
            }
        }

        setSelectedTab(binding.tabLayout, tabPosition)

        tabLayout.tabMode = TabLayout.MODE_FIXED

        tabLayout.addOnTabSelectedListener(object :TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.position?.let { tabPosition = it }
                initSpinner(binding.etFirNum)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })

        initSpinner(binding.etFirNum)



        return binding.root
    }
    private fun setSelectedTab(tabLayout: TabLayout, selectedTabIndex: Int) {
        val tabCount = tabLayout.tabCount
        if (selectedTabIndex >= 0 && selectedTabIndex < tabCount) {
            val tab = tabLayout.getTabAt(selectedTabIndex)
            tab?.select()
        }
    }

    private fun initSpinner(spinner : Spinner) {
        //spinner

        val items = resources.getStringArray(R.array.spinner_array).toMutableList()
        val spinneradapter = CustomSpinnerAdapter(requireContext(), items)
        spinneradapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_closed)
        spinner.adapter = spinneradapter


        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                //아이템이 클릭 되면 맨 위부터 position 0번부터 순서대로 동작하게 됩니다.
                when (position) {
                    0 -> {
                        spinnerString = "인기순"
                    }

                    1 -> {
                        spinnerString = "추천순"
                    }
                    2->{
                        spinnerString = "가격높은순"
                    }
                    3 -> {
                        spinnerString = "가격낮은순"
                    }
                }
                retrofitCall()
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }
    }

    private fun retrofitCall() {
        if(category){
            RetrofitManager.instance.experienceGiftSttCategory(categoryId = tabPosition+1, category = spinnerString // category 번호로 바꾸기
            ) {
                    responseState, responseBody ->
                when (responseState) {
                    RESPONSE_STATE.OKAY -> {
                        val productDataList = ArrayList<ProductData>()
                        if (responseBody != null) {
                            for (experienceResNode in responseBody) {
                                val title: String = experienceResNode.title ?: ""
                                val subtitle: String = experienceResNode.subtitle ?: ""
                                val price: Int = experienceResNode.price ?: 0
                                val formattedPrice = String.format("%,d", price.toInt())
                                val giftImgUrl : String
                                if(experienceResNode.giftImgUrl.size == 0){
                                    giftImgUrl = ""
                                }
                                else{
                                    giftImgUrl = experienceResNode.giftImgUrl[0] ?: ""
                                }
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
                        initProductRecycler(rvProduct,productDataList)

                    }

                    RESPONSE_STATE.FAIL -> {
                        Log.d("retrofit", "api 호출 에러")
                    }
                }

            }
        }
        else {
            RetrofitManager.instance.experienceGiftExpCategory(categoryId = tabPosition+1, category = spinnerString, completion = { // 카테고리별 경험으로 바꾸기
                    responseState, responseBody ->
                when(responseState){
                    RESPONSE_STATE.OKAY -> {
                        Log.d("retrofit", "api 호출 성공1 : ${responseBody!!}")
                        val productDataList = ArrayList<ProductData>()
                        if (responseBody != null) {
                            for (experienceResNode in responseBody) {
                                val title: String = experienceResNode.title?: ""
                                val subtitle: String = experienceResNode.subtitle?: ""
                                val price: Int = experienceResNode.price?: 0
                                val formattedPrice = String.format("%,d", price.toInt())
                                val giftImgUrl : String
                                if(experienceResNode.giftImgUrl.size == 0){
                                    giftImgUrl = ""
                                }
                                else{
                                    giftImgUrl = experienceResNode.giftImgUrl[0] ?: ""
                                }
                                val giftid: Int = experienceResNode.experienceGiftId?: 0
                                // ProductData 객체를 ArrayList에 추가
                                productDataList.add(ProductData(title=title, subtitle = subtitle, price = formattedPrice, img = giftImgUrl, giftid = giftid))
                            }
                        }
                        initProductRecycler(rvProduct, productDataList)
                    }
                    RESPONSE_STATE.FAIL -> {
                        Log.d("retrofit", "api 호출 에러")
                    }
                }
            })
        }
    }
}