package com.shall_we.Drawer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.shall_we.R
import com.shall_we.databinding.FragmentDrawerHelpBinding
import com.shall_we.home.HomeRecomFragment
import com.shall_we.home.dpToPx

class DrawerHelpFragment : Fragment() {
    lateinit var drawerAdapter: DrawerAdapter
    val drawerData = mutableListOf<DrawerData>()

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
        rv.adapter = drawerAdapter


        drawerData.apply {
            add(DrawerData(img = R.drawable.category_faq, name = "FAQ"))
            add(DrawerData(img = R.drawable.category_mail, name = "메일 문의"))



            drawerAdapter.datas = drawerData
            drawerAdapter.notifyDataSetChanged()



        }
        val spaceDecoration = HomeRecomFragment.HorizontalSpaceItemDecoration(dpToPx(7))
        rv.addItemDecoration(spaceDecoration)


    }
}