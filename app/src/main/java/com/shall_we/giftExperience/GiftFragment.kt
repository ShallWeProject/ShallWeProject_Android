package com.shall_we.giftExperience

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.createBitmap
import com.shall_we.mypage.MypageFragment
import com.shall_we.R
import com.shall_we.base.BaseFragment
import com.shall_we.databinding.FragmentExperienceDetailBinding
import com.shall_we.databinding.FragmentGiftBinding
import com.shall_we.home.HomeFragment
import com.shall_we.mypage.MyGiftReceivedFragment
import com.shall_we.search.SearchFragment


class GiftFragment : BaseFragment<FragmentGiftBinding>(R.layout.fragment_gift) {




    override fun init() {
        binding.giftBtn01.setOnClickListener()
        {

            val homeFragment = HomeFragment() // 전환할 프래그먼트 인스턴스 생성
            val fragmentTransaction = parentFragmentManager.beginTransaction()
            // 기존 프래그먼트를 숨기고 새로운 프래그먼트로 교체
            fragmentTransaction.replace(R.id.home_layout, homeFragment, "home")

            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commitAllowingStateLoss()

        }
        binding.giftBtn02.setOnClickListener()
        {
            Log.d("btn","clicked")
            val myPageFragment = MypageFragment() // 전환할 프래그먼트 인스턴스 생성
            val fragmentTransaction = parentFragmentManager.beginTransaction()
            // 기존 프래그먼트를 숨기고 새로운 프래그먼트로 교체
            fragmentTransaction.replace(R.id.giftLayout, myPageFragment, "mypage")

            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commitAllowingStateLoss()

        }

    }



}
