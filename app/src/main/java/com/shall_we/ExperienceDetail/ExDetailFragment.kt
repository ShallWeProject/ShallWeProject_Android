package com.shall_we.ExperienceDetail

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.shall_we.R
import com.shall_we.base.BaseFragment
import com.shall_we.databinding.FragmentExDetailBinding
import com.shall_we.databinding.FragmentExperienceDetailBinding
import com.shall_we.retrofit.RESPONSE_STATE


class ExDetailFragment : Fragment() {
    lateinit var experienceDetailViewModel: ExperienceDetailViewModel
    private var experienceGiftId: Int = 1
    private lateinit var binding: FragmentExDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentExDetailBinding.inflate(inflater, container, false)

        arguments?.let {
            experienceGiftId = it.getInt("id")
        }

        experienceDetailViewModel = ViewModelProvider(this).get(ExperienceDetailViewModel::class.java)
        experienceDetailViewModel.get_experience_detail_data(experienceGiftId) { responseState, responseBody ->
            when (responseState) {
                RESPONSE_STATE.OKAY -> {
                    Log.d("whatisthis?????", responseBody.toString())
                    responseBody?.let { item ->
                        binding.exdetailDescription.text = item.description
                        if(item.explanation.size != 0){
                            binding.firStage.text = "1단계: "+item.explanation[0].stage
                            binding.firDescription.text = item.explanation[0].description
                            Glide.with(this).load(item.explanation[0].explanationUrl).into(binding.firImage)

                            binding.secStage.text = "2단계: "+item.explanation[1].stage
                            binding.secDescription.text = item.explanation[1].description
                            Glide.with(this).load(item.explanation[1].explanationUrl).into(binding.secImage)

                            binding.thrStage.text = "3단계: "+item.explanation[2].stage
                            binding.thrDescription.text = item.explanation[2].description
                            Glide.with(this).load(item.explanation[2].explanationUrl).into(binding.thrImage)

                            binding.location.text = item.location
                        }
                    }
                }

                RESPONSE_STATE.FAIL -> {
                    Log.d("retrofit", "api 호출 에러")
                }
            }
        }
        return binding.root
    }
}


