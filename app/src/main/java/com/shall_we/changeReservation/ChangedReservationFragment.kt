package com.shall_we.changeReservation

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.shall_we.R
import com.shall_we.base.BaseFragment
import com.shall_we.databinding.FragmentChangedReservationBinding
import com.shall_we.home.HomeFragment
import com.shall_we.mypage.MyGiftReceivedFragment
import com.shall_we.mypage.MypageFragment


class ChangedReservationFragment : BaseFragment<FragmentChangedReservationBinding>(R.layout.fragment_changed_reservation) {


    override fun init() {
        binding.giftBtn01.setOnClickListener()
        {

            val homeFragment = HomeFragment() // 전환할 프래그먼트 인스턴스 생성
            val fragmentTransaction = parentFragmentManager.beginTransaction()
            // 기존 프래그먼트를 숨기고 새로운 프래그먼트로 교체
            fragmentTransaction.add(R.id.mypage_layout, homeFragment, "home")

            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commitAllowingStateLoss()

        }
        binding.giftBtn02.setOnClickListener()
        {
            Log.d("btn","clicked")
            binding.giftBtn02.visibility=View.GONE
            binding.giftBtn01.visibility=View.GONE
            binding.gifted.visibility=View.GONE
            binding.giftText01.visibility=View.GONE
            binding.giftText02.visibility=View.GONE
            val myPageFragment = MypageFragment() // 전환할 프래그먼트 인스턴스 생성
            val fragmentTransaction = parentFragmentManager.beginTransaction()
            // 기존 프래그먼트를 숨기고 새로운 프래그먼트로 교체
            fragmentTransaction.replace(R.id.nav_host_fragment,myPageFragment , "mypage")

            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commitAllowingStateLoss()

        }

    }



}