package com.shall_we.ExperienceDetail

import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayoutMediator
import com.shall_we.R
import com.shall_we.base.BaseFragment
import com.shall_we.databinding.FragmentExperienceDetailBinding
import com.shall_we.giftExperience.GiftResevationFragment
import com.shall_we.giftExperience.ReservationViewModel
import com.shall_we.retrofit.RESPONSE_STATE
import com.shall_we.retrofit.RetrofitManager
import com.shall_we.utils.initProductRecycler
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ExperienceDetailFragment: BaseFragment<FragmentExperienceDetailBinding>(R.layout.fragment_experience_detail)  {
    lateinit var experienceDetailViewModel: ExperienceDetailViewModel
    lateinit var reservationViewModel: ReservationViewModel
    override fun init() {

        initTab()

        val giftid = arguments?.getInt("id", 1)
        Log.d("id","$giftid")


        experienceDetailViewModel = ViewModelProvider(this).get(ExperienceDetailViewModel::class.java)
       reservationViewModel = ViewModelProvider(this).get(ReservationViewModel::class.java)
        experienceDetailViewModel.get_experience_detail_data(1, completion = {
                responseState, responseBody ->
            when(responseState){
                RESPONSE_STATE.OKAY -> {
                    Log.d("whatisthis", responseBody.toString())

                }
                RESPONSE_STATE.FAIL -> {
                    Log.d("retrofit", "api 호출 에러")
                }
            }
        })



       experienceDetailViewModel.get_experience_gift()
       reservationViewModel.get_reservation( completion = {
               responseState, responseBody ->
           when(responseState){
               RESPONSE_STATE.OKAY -> {
                   Log.d("what??????????", responseBody.toString())

               }
               RESPONSE_STATE.FAIL -> {
                   Log.d("retrofit", "api 호출 에러")
               }
           }
       })




       // experienceDetailViewModel.set_experience_gift(ExperienceReq(


      //  ))








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