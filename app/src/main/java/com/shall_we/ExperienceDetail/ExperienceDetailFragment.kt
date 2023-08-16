package com.shall_we.ExperienceDetail

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import com.shall_we.R
import com.shall_we.base.BaseFragment
import com.shall_we.databinding.FragmentExperienceDetailBinding
import com.shall_we.databinding.FragmentHomeBinding
import com.shall_we.giftExperience.GiftExperienceFragment
import com.shall_we.giftExperience.GiftResevationFragment


class ExperienceDetailFragment: BaseFragment<FragmentExperienceDetailBinding>(R.layout.fragment_experience_detail)  {
    override fun init() {
     //   initAppbar(binding.exdetailToolbar, "경험상세", true, false)
        initTab()

        val giftid = arguments?.getInt("id", 1)

        Log.d("id","$giftid")


        binding.fab.setOnClickListener()
        {
            Log.d("clicked","clicked")
            binding.fab.visibility = View.GONE
            val giftReservationFragment = GiftResevationFragment() // 전환할 프래그먼트 인스턴스 생성
            val fragmentTransaction = parentFragmentManager.beginTransaction()
            // 기존 프래그먼트를 숨기고 새로운 프래그먼트로 교체
            fragmentTransaction.replace(R.id.exdetail_layout, giftReservationFragment, "giftreserve")

            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commitAllowingStateLoss()

        }

    }

    private fun initTab() {

        val mainVPAdapter = super.getActivity()?.let { ExDetailVPAdapter(fragmentActivity = it) }
        binding.vpMain.adapter = mainVPAdapter

        val tabTitleArray = arrayOf(
            "설명",
            "안내",
        )
        TabLayoutMediator(binding.tabMain, binding.vpMain) { tab, position ->
            tab.text = tabTitleArray[position]
        }.attach()


    }

}