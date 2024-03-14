package com.shall_we.ExperienceDetail

import GiftDTO
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.shall_we.databinding.FragmentExDetailBinding

class ExDetailFragment : Fragment() {
    private var experienceGiftId: Int = 1
    private lateinit var binding: FragmentExDetailBinding

    lateinit var experienceDetailRes: GiftDTO

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentExDetailBinding.inflate(inflater, container, false)

        arguments?.let {
            experienceGiftId = it.getInt("id")
            experienceDetailRes = it.getParcelable<GiftDTO>("experienceDetailRes")!!
        }
        if(experienceDetailRes != null){
            binding.exdetailDescription.text = experienceDetailRes.description
            if(experienceDetailRes.explanation.size != 0){
                binding.firStage.text = "1단계: "+experienceDetailRes.explanation[0].stage
                binding.firDescription.text = experienceDetailRes.explanation[0].description
                Glide.with(this).load(experienceDetailRes.explanation[0].explanationUrl).into(binding.firImage)

                binding.secStage.text = "2단계: "+experienceDetailRes.explanation[1].stage
                binding.secDescription.text = experienceDetailRes.explanation[1].description
                Glide.with(this).load(experienceDetailRes.explanation[1].explanationUrl).into(binding.secImage)

                binding.thrStage.text = "3단계: "+experienceDetailRes.explanation[2].stage
                binding.thrDescription.text = experienceDetailRes.explanation[2].description
                Glide.with(this).load(experienceDetailRes.explanation[2].explanationUrl).into(binding.thrImage)

                binding.location.text = experienceDetailRes.location
            }
        }
        return binding.root
    }
}


