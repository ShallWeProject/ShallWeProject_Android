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

                    responseBody?.get(0)?.let { item ->
                        item.explanation?.forEach { explanation ->

                            // Add text view
                            val textView = TextView(requireContext())
                            textView.text = explanation.description
                            textView.textSize = 14f
                            textView.textAlignment = View.TEXT_ALIGNMENT_CENTER
                            textView.setTextColor(Color.BLACK)
                            textView.setPadding(0, 16, 0, 16)
                            binding.imageContainer.addView(textView)

                            // Add image view
                            val imageView = ImageView(requireContext())
                            Glide.with(this)
                                .load(explanation.explanationUrl)
                                .into(imageView)
                            imageView.setPadding(0, 16, 0, 16)
                            binding.imageContainer.addView(imageView)
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


