package com.shall_we.drawer

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.shall_we.MainActivity
import com.shall_we.R
import com.shall_we.databinding.FragmentDrawerHelpBinding
import com.shall_we.mypage.FaqFragment
import com.shall_we.utils.HorizontalSpaceItemDecoration
import com.shall_we.utils.dpToPx

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
            parentFragmentManager.beginTransaction()
                .add(R.id.nav_host_fragment, newFragment)
                .addToBackStack(null)
                .commit()
        }
        else {
            sendEmail()
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
        val spaceDecoration = HorizontalSpaceItemDecoration(dpToPx(15))
        rv.addItemDecoration(spaceDecoration)
    }
    private fun sendEmail() {
        val emailIntent = Intent(Intent.ACTION_SEND)

        try {
            emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf("connect.shallwe@gmail.com"))
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "[메일 문의]")
            emailIntent.type = "text/html"
            emailIntent.setPackage("com.google.android.gm")
            if (emailIntent.resolveActivity(requireActivity().packageManager) != null) startActivity(emailIntent)
            startActivity(emailIntent)
        } catch (e: Exception) {
            e.printStackTrace()
            emailIntent.type = "text/html"
            emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf("connect.shallwe@gmail.com"))
            startActivity(Intent.createChooser(emailIntent, "Send Email"))
        }
    }



}