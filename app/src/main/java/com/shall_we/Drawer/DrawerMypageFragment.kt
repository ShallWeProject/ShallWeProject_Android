package com.shall_we.Drawer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.shall_we.R
import com.shall_we.databinding.FragmentDrawerMypageBinding
import com.shall_we.home.HomeRecomFragment
import com.shall_we.home.dpToPx

class DrawerMypageFragment : Fragment() {


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
        lateinit var drawerAdapter: DrawerAdapter
        val drawerData = mutableListOf<DrawerData>()
        drawerAdapter = DrawerAdapter(requireContext())
        rv.adapter = drawerAdapter


        drawerData.apply {
            add(DrawerData(img = R.drawable.category_photobook, name = "추억사진첩"))
            add(DrawerData(img = R.drawable.category_sent, name = "보낸 선물"))
            add(DrawerData(img = R.drawable.category_received, name = "빋은 선물"))



            drawerAdapter.datas = drawerData
            drawerAdapter.notifyDataSetChanged()



        }
        val spaceDecoration = HomeRecomFragment.HorizontalSpaceItemDecoration(dpToPx(15))
        rv.addItemDecoration(spaceDecoration)


    }
}
