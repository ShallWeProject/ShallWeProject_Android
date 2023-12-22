package com.shall_we.giftExperience

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.shall_we.ExperienceDetail.ExperienceDetailViewModel
import com.shall_we.R
import com.shall_we.databinding.FragmentGiftBinding
import com.shall_we.dto.LocalTime
import com.shall_we.dto.ReservationRequest
import com.shall_we.dto.ReservationStatus
import com.shall_we.home.HomeFragment
import com.shall_we.mypage.MyGiftSentFragment


class GiftFragment : Fragment() {
    private lateinit var binding: FragmentGiftBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGiftBinding.inflate(inflater, container, false)  // Binding 객체 초기화

        binding.giftBtn01.setOnClickListener() {
            val homeFragment = HomeFragment() // 전환할 프래그먼트 인스턴스 생성
            val fragmentTransaction = parentFragmentManager.beginTransaction()
            // 기존 프래그먼트를 숨기고 새로운 프래그먼트로 교체
            fragmentTransaction.replace(R.id.nav_host_fragment, homeFragment, "home")
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commitAllowingStateLoss()
        }

        binding.giftBtn02.setOnClickListener() {
            Log.d("btn","clicked")
            binding.giftBtn02.visibility=View.GONE
            binding.giftBtn01.visibility=View.GONE
            val myGiftSentFragment = MyGiftSentFragment() // 전환할 프래그먼트 인스턴스 생성
            val fragmentTransaction = parentFragmentManager.beginTransaction()
            // 기존 프래그먼트를 숨기고 새로운 프래그먼트로 교체
            fragmentTransaction.replace(R.id.nav_host_fragment, myGiftSentFragment, "mypage")

            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commitAllowingStateLoss()

        }
        return binding.root
    }


}
