package com.shall_we.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.LinearLayout
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.shall_we.R
import com.shall_we.databinding.FragmentProductListBinding
import com.shall_we.retrofit.RESPONSE_STATE
import com.shall_we.retrofit.RetrofitManager
import com.shall_we.utils.initProductRecycler


class ProductListFragment : Fragment() {
    var category : Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentProductListBinding.inflate(inflater,container,false)
        // 포지션 가져오기 & 선택한 탭으로 초기화
        val position = arguments?.getInt("position", 0)
        val tab = arguments?.getString("tab", "경험카테고리")

        //tabbar 설정
        val tabLayout: TabLayout = binding.tabLayout
        val tabCount: Int = tabLayout.tabCount
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
        if(tab == "경험카테고리"){
            val tabArray = arrayOf("공예","베이킹","문화예술","아웃도어","스포츠")
            category = false
            for (i in 0 until tabLayout.tabCount) {
                val tab = tabLayout.getTabAt(i)
                tab?.text = tabArray[i].toString()
            }
        }
        else {
            val tabArray = arrayOf("생일","연인","부모님","입학/졸업","결혼/집들이")
            category = true
            for (i in 0 until tabLayout.tabCount) {
                val tab = tabLayout.getTabAt(i)
                tab?.text = tabArray[i].toString()
            }
        }

        setSelectedTab(binding.tabLayout, position ?: 0)
        val data = retrofitCall(category, 1,binding.rvProduct)

        initSpinner(binding.spinner)




        tabLayout.tabMode = TabLayout.MODE_FIXED

        tabLayout.addOnTabSelectedListener(object :TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.position?.let { retrofitCall(category, it, binding.rvProduct) }
                initSpinner(binding.spinner)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })


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
                        Log.d("spinner", "0")
                    }

                    1 -> {
                        Log.d("spinner", "1")

                    }
                    //...
                    else -> {
                        Log.d("spinner", "---")

                    }
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }

    }

    private fun retrofitCall(sttCategory : Boolean, categoryId : Int, rv:RecyclerView) {
        if(sttCategory == true){
            RetrofitManager.instance.experienceGiftSttCategory(categoryId = 1, category = "가격높은순" // category 번호로 바꾸기
            ) {
                    responseState, responseBody ->
                when (responseState) {
                    RESPONSE_STATE.OKAY -> {
                        Log.d("retrofit", "api 호출 성공1 : ${responseBody!!}")
                        initProductRecycler(rv,responseBody)
                    }

                    RESPONSE_STATE.FAIL -> {
                        Log.d("retrofit", "api 호출 에러")
                    }
                }

            }
        }
        else {
//            RetrofitManager.instance.experienceGiftExpCategory(categoryId = categoryId, category = "가격높은순" ) {
//                    responseState, responseBody ->
//                when (responseState) {
//                    RESPONSE_STATE.OKAY -> {
//                        Log.d("retrofit", "api 호출 성공1 : ${responseBody!!}")
//                        initProductRecycler(rv,responseBody)
//                    }
//
//                    RESPONSE_STATE.FAIL -> {
//                        Log.d("retrofit", "api 호출 에러")
//                    }
//                }

//            }
        }
    }

}