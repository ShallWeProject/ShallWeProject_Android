package com.shall_we.drawer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.shall_we.MainActivity
import com.shall_we.mypage.MypageFragment
import com.shall_we.R
import com.shall_we.databinding.FragmentDrawerMypageBinding
import com.shall_we.home.HomeRecomFragment
import com.shall_we.utils.HorizontalSpaceItemDecoration
import com.shall_we.utils.dpToPx

class DrawerMypageFragment : Fragment(), DrawerAdapter.OnItemClickListener {
    lateinit var drawerAdapter: DrawerAdapter
    val drawerData = mutableListOf<DrawerData>()



    override fun onItemClick(position: Int) {
        MainActivity.MySharedData.pageFlag = 2

        val navigationView = requireActivity().findViewById<NavigationView>(R.id.main_navigation_view)
        val drawerLayout = navigationView.parent as DrawerLayout
        drawerLayout.closeDrawer(GravityCompat.START)

        // 클릭된 아이템의 정보를 사용하여 다른 프래그먼트로 전환하는 로직을 작성
        val newFragment = MypageFragment() // 전환할 다른 프래그먼트 객체 생성
        val bundle = Bundle()
        bundle.putString("tab", "경험카테고리")
        bundle.putInt("position", position) // 클릭된 아이템의 이름을 "name" 키로 전달
        newFragment.arguments = bundle

        // 프래그먼트 전환
        parentFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment, newFragment)
            .addToBackStack(null)
            .commit()

        val bottomNavigation = requireActivity().findViewById<BottomNavigationView>(R.id.nav_bottom)
        bottomNavigation.menu.findItem(R.id.menu_mypage).setChecked(true)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentDrawerMypageBinding.inflate(inflater,container,false)
        initRecycler(binding.rvMypage)
        return binding.root
    }

    fun initRecycler(rv: RecyclerView) {

        drawerAdapter = DrawerAdapter(requireContext())
        drawerAdapter.setOnItemClickListener(this)

        rv.adapter = drawerAdapter


        drawerData.apply {
            add(DrawerData(img = R.drawable.category_photobook, name = "추억사진첩"))
            add(DrawerData(img = R.drawable.category_sent, name = "보낸 선물"))
            add(DrawerData(img = R.drawable.category_received, name = "빋은 선물"))



            drawerAdapter.datas = drawerData
            drawerAdapter.notifyDataSetChanged()



        }
        val spaceDecoration = HorizontalSpaceItemDecoration(dpToPx(15))
        rv.addItemDecoration(spaceDecoration)


    }
}
