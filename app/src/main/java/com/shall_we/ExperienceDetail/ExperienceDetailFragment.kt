package com.shall_we.ExperienceDetail

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bumptech.glide.Glide
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
    private var experienceGiftId: Int = 1
    lateinit var exDetailFragment: ExDetailFragment

    override fun init() {



        arguments?.let { // 아규먼트로부터 데이터를 불러옴

            experienceGiftId = it.getInt("id") // id 키로 giftid 값을 불러와 저장하게 됩니다.


        }

        initTab()




        experienceDetailViewModel = ViewModelProvider(this).get(ExperienceDetailViewModel::class.java)
        reservationViewModel = ViewModelProvider(this).get(ReservationViewModel::class.java)

        experienceDetailViewModel.get_experience_detail_data(experienceGiftId, completion = {
                responseState, responseBody ->
            when(responseState){
                RESPONSE_STATE.OKAY -> {


                    responseBody?.get(0)?.let { item ->
                        binding.exdetailText01.text = item.subtitle
                        binding.exdetailText03.text = item.title
                        binding.exdetailText04.text=item.price.toString()
                        //val explanationsText = item.explanation?.joinToString(separator = "\n") {
                        //    it.description.toString()
                        //}
                        //binding.exdetailText04.text =explanationsText
                        Glide.with(this)
                            .load(item.giftImageUrl)
                            .into(binding.exdetailImage)



                    }


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
        val bundle = arguments
        val mainVPAdapter = activity?.let { ExDetailVPAdapter(fragmentActivity = it, bundle = bundle) }

        //val mainVPAdapter = super.getActivity()?.let { ExDetailVPAdapter(fragmentActivity = it) }
        binding.vpMain.adapter = mainVPAdapter

        val tabTitleArray = arrayOf(
            "설명",
            "안내",
        )
        TabLayoutMediator(binding.tabMain, binding.vpMain) { tab, position ->
            tab.text = tabTitleArray[position]
        }.attach()


    }

    class ExDetailVPAdapter(private val fragmentActivity: FragmentActivity, private val bundle: Bundle?) :
        FragmentStateAdapter(fragmentActivity) {

        override fun getItemCount(): Int = 2 // 탭 수에 맞게 변경해주세요.

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> {
                    ExDetailFragment().apply {
                        arguments = this@ExDetailVPAdapter.bundle
                }}
                1 -> {
                   ExDetailPresentFragment().apply {
                       arguments=this@ExDetailVPAdapter.bundle
                   }
                }
                else -> throw IllegalArgumentException("Invalid position: $position")
            }
        }
    }



}