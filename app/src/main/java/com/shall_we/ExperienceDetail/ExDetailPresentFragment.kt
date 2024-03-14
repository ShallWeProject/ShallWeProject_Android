package com.shall_we.ExperienceDetail

import GiftDTO
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.shall_we.databinding.FragmentExDetailPresentBinding

class ExDetailPresentFragment : Fragment() {

    private lateinit var experienceDetailRes: GiftDTO
    private lateinit var binding: FragmentExDetailPresentBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentExDetailPresentBinding.inflate(layoutInflater)
        arguments?.let {
            experienceDetailRes = it.getParcelable<GiftDTO>("experienceDetailRes")!!
        }

        if(experienceDetailRes.note != null){
            binding.tvNote.text = experienceDetailRes.note
        }
        return binding.root
    }

}