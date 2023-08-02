package com.shall_we.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.shall_we.R
import com.shall_we.databinding.FragmentProductListBinding


class ProductListFragment : Fragment() {
    lateinit var productAdapter: ProductAdapter
    val productData = mutableListOf<ProductData>()

    companion object {
        fun newInstance(position: Int): ProductListFragment {
            val fragment = ProductListFragment()
            val args = Bundle()
            args.putInt("position", position)
            fragment.arguments = args
            return fragment
        }
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
        // 포지션 가져오기 & 선택한 탭으로 초기화
        val position = arguments?.getInt("position", 0)
        setSelectedTab(binding.tabLayout, position ?: 0)

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
        initRecycler(binding.rvProduct)
        initSpinner(binding.spinner)




        tabLayout.tabMode = TabLayout.MODE_FIXED

        tabLayout.addOnTabSelectedListener(object :TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                initSpinner(binding.spinner)
                initRecycler(binding.rvProduct)
                when(tab?.position){
                    0 -> {

                    }
                    1 -> {

                    }
                    2 -> {

                    }
                    3 -> {

                    }
                    4 -> {

                    }
                }
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

    private fun initRecycler(rvProduct: RecyclerView) {
        productAdapter = ProductAdapter(requireContext())
        val layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
        rvProduct.layoutManager = layoutManager
        rvProduct.adapter = productAdapter

        productData.apply {
            add(
                ProductData(
                    name = "[성수] 인기 공예 클래스",
                    comment = "프라이빗 소수정예 터프팅 클래스",
                    price = "39,800 원",
                    img = R.drawable.product_img
                )
            )
            add(
                ProductData(
                    name = "[성수] 인기 공예 클래스",
                    comment = "프라이빗 소수정예 터프팅 클래스",
                    price = "39,800 원",
                    img = R.drawable.product_img
                )
            )
            add(
                ProductData(
                    name = "[성수] 인기 공예 클래스",
                    comment = "프라이빗 소수정예 터프팅 클래스",
                    price = "39,800 원",
                    img = R.drawable.product_img
                )
            )
            add(
                ProductData(
                    name = "[성수] 인기 공예 클래스",
                    comment = "프라이빗 소수정예 터프팅 클래스",
                    price = "39,800 원",
                    img = R.drawable.product_img
                )
            )
            add(
                ProductData(
                    name = "[성수] 인기 공예 클래스",
                    comment = "프라이빗 소수정예 터프팅 클래스",
                    price = "39,800 원",
                    img = R.drawable.product_img
                )
            )
            add(
                ProductData(
                    name = "[성수] 인기 공예 클래스",
                    comment = "프라이빗 소수정예 터프팅 클래스",
                    price = "39,800 원",
                    img = R.drawable.product_img
                )
            )
            add(
                ProductData(
                    name = "[성수] 인기 공예 클래스",
                    comment = "프라이빗 소수정예 터프팅 클래스",
                    price = "39,800 원",
                    img = R.drawable.product_img
                )
            )



            productAdapter.datas = productData
            productAdapter.notifyDataSetChanged()
//            rvRealtime.addItemDecoration(GridSpaceItemDecoration(2, dpToPx(8)))
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

}