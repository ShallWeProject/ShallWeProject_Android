package com.shall_we.drawer

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.shall_we.MainActivity
import com.shall_we.R
import com.shall_we.databinding.FragmentDrawerExBinding
import com.shall_we.home.ProductListFragment
import com.shall_we.home.dpToPx
import com.shall_we.utils.SpaceItemDecoration

class DrawerExFragment : Fragment(), DrawerAdapter.OnItemClickListener {

    lateinit var drawerAdapter: DrawerAdapter
    val drawerData = mutableListOf<DrawerData>()

    override fun onItemClick(position: Int) {
        MainActivity.MySharedData.pageFlag = 1

        val navigationView = requireActivity().findViewById<NavigationView>(R.id.main_navigation_view)
        val drawerLayout = navigationView.parent as DrawerLayout
        drawerLayout.closeDrawer(GravityCompat.START)

        // 클릭된 아이템의 정보를 사용하여 다른 프래그먼트로 전환하는 로직을 작성
        val newFragment = ProductListFragment() // 전환할 다른 프래그먼트 객체 생성
        val bundle = Bundle()
        bundle.putString("tab", "경험카테고리")
        bundle.putInt("position", position) // 클릭된 아이템의 이름을 "name" 키로 전달
        newFragment.arguments = bundle

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
        val binding = FragmentDrawerExBinding.inflate(inflater,container,false)
        initRecycler(binding.rvEx)
        return binding.root
    }

    fun initRecycler(rv: RecyclerView) {

        drawerAdapter = DrawerAdapter(requireContext())
        drawerAdapter.setOnItemClickListener(this)

        val layoutManager = GridLayoutManager(context, 4, GridLayoutManager.VERTICAL, false)
        rv.layoutManager = layoutManager
        rv.adapter = drawerAdapter


        drawerData.apply {
            add(DrawerData(img = R.drawable.craft, name = "공예"))
            add(DrawerData(img = R.drawable.baking, name = "베이킹"))
            add(DrawerData(img = R.drawable.art, name = "문화예술"))
            add(DrawerData(img = R.drawable.outdoor, name = "아웃도어"))
            add(DrawerData(img = R.drawable.sport, name = "스포츠"))


            drawerAdapter.datas = drawerData
            drawerAdapter.notifyDataSetChanged()



        }
        val spaceDecoration = SpaceItemDecoration(dpToPx(15), dpToPx(13))

        rv.addItemDecoration(spaceDecoration)


    }

}
