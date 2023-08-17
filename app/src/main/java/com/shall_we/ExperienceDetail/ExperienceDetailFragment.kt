package com.shall_we.ExperienceDetail

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayoutMediator
import com.shall_we.R
import com.shall_we.base.BaseFragment
import com.shall_we.databinding.FragmentExperienceDetailBinding
import com.shall_we.databinding.FragmentHomeBinding
import com.shall_we.dto.ExperienceGiftDto
import com.shall_we.dto.ExperienceReq
import com.shall_we.giftExperience.GiftExperienceFragment
import com.shall_we.giftExperience.GiftResevationFragment
import com.shall_we.retrofit.RESPONSE_STATE
import com.shall_we.retrofit.RetrofitManager
import com.shall_we.search.SearchResultFragment
import com.shall_we.utils.initProductRecycler
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ExperienceDetailFragment: BaseFragment<FragmentExperienceDetailBinding>(R.layout.fragment_experience_detail)  {
    lateinit var experienceDetailViewModel: ExperienceDetailViewModel
    override fun init() {

        initTab()

        val giftid = arguments?.getInt("id", 1)
        Log.d("id","$giftid")

        experienceDetailViewModel = ViewModelProvider(requireActivity()).get(ExperienceDetailViewModel::class.java)
        experienceDetailViewModel.get_experience_detail_data(id)
        experienceDetailViewModel.experience_detail_data.observe(viewLifecycleOwner, Observer {
            now_experience_detail_data->
            if(now_experience_detail_data!=null){
                //binding.exdetailText01.text=now_experience_detail_data.title.toString()
                //binding.exdetailText02.text=now_experience_detail_data.subtitle.toString()

            }
        })

        experienceDetailViewModel.get_experience_gift()
        experienceDetailViewModel.experience_gift_data.observe(viewLifecycleOwner, Observer {
            now_experience_detail_data->
           // binding.exdetailText01.text=now_experience_detail_data.expCategories

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