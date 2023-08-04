package com.shall_we.Drawer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.shall_we.MainActivity
import com.shall_we.R
import com.shall_we.databinding.FragmentDrawerHelpBinding
import com.shall_we.home.HomeRecomFragment
import com.shall_we.home.dpToPx
import com.shall_we.mypage.FaqFragment

class DrawerHelpFragment : Fragment(), DrawerAdapter.OnItemClickListener  {
    lateinit var drawerAdapter: DrawerAdapter
    val drawerData = mutableListOf<DrawerData>()

    override fun onItemClick(position: Int) {
        MainActivity.MySharedData.pageFlag = 1

        val navigationView = requireActivity().findViewById<NavigationView>(R.id.main_navigation_view)
        val drawerLayout = navigationView.parent as DrawerLayout
        drawerLayout.closeDrawer(GravityCompat.START)
        val newFragment : Fragment
        if(position == 0){
            newFragment = FaqFragment() // faq페이지로 이동
            val bundle = Bundle()
            newFragment.arguments = bundle

        }
        else {
            newFragment = FaqFragment() // 메일 문의 페이지로 이동 **수정하기
            val bundle = Bundle()
            newFragment.arguments = bundle
        }

        // 프래그먼트 전환
        parentFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment, newFragment)
            .addToBackStack(null)
            .commit()

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentDrawerHelpBinding.inflate(inflater,container,false)
        initRecycler(binding.rvHelp)

        return binding.root
    }

    fun initRecycler(rv: RecyclerView) {
        drawerAdapter = DrawerAdapter(requireContext())
        drawerAdapter.setOnItemClickListener(this)

        rv.adapter = drawerAdapter


        drawerData.apply {
            add(DrawerData(img = R.drawable.category_faq, name = "FAQ"))
            add(DrawerData(img = R.drawable.category_mail, name = "메일 문의"))



            drawerAdapter.datas = drawerData
            drawerAdapter.notifyDataSetChanged()



        }
        val spaceDecoration = HomeRecomFragment.HorizontalSpaceItemDecoration(dpToPx(15))
        rv.addItemDecoration(spaceDecoration)


    }
}