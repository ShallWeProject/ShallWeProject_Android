package com.shall_we.ExperienceDetail

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.shall_we.R
import com.shall_we.databinding.FragmentExperienceDetailBinding
import com.shall_we.giftExperience.GiftResevationFragment
import com.shall_we.giftExperience.ReservationViewModel
import com.shall_we.retrofit.RESPONSE_STATE


class ExperienceDetailFragment: Fragment() {
    private lateinit var binding: FragmentExperienceDetailBinding
    lateinit var experienceDetailViewModel: ExperienceDetailViewModel
    lateinit var reservationViewModel: ReservationViewModel
    private var experienceGiftId: Int = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentExperienceDetailBinding.inflate(inflater, container, false)

        // binding 초기화 후 initTab 함수 호출
        initTab()

        arguments?.let { // 아규먼트로부터 데이터를 불러옴
            experienceGiftId = it.getInt("id") // id 키로 giftid 값을 불러와 저장하게 됩니다.
        }

        experienceDetailViewModel = ViewModelProvider(this).get(ExperienceDetailViewModel::class.java)
       // reservationViewModel = ViewModelProvider(this).get(ReservationViewModel::class.java)

        experienceDetailViewModel.get_experience_detail_data(
            experienceGiftId,
            completion = { responseState, responseBody ->
                when (responseState) {
                    RESPONSE_STATE.OKAY -> {
                        responseBody?.get(0)?.let { item ->
                            Log.d("exitem",item.toString())
                            binding.exdetailText01.text = item.subtitle
                            binding.exdetailText03.text = item.title
                            binding.exdetailText04.text = item.price.toString()

                            // 이미지 URL 리스트를 ViewPager의 Adapter에 전달
                            item.giftImageUrl?.let { imageUrlList ->
                                val adapter = ImageAdapter(requireContext(), imageUrlList)
                                binding.exdetailImage.adapter = adapter
                            }
                        }
                    }
                    RESPONSE_STATE.FAIL -> {
                        Log.d("retrofit", "api 호출 에러")
                    }
                }
            }
        )







        binding.fab.setOnClickListener() {
            binding.fab.visibility = View.GONE

            val bundle = Bundle().apply {
                putInt("id", experienceGiftId) // 클릭된 아이템의 이름을 "title" 키로 전달
            }

            val giftReservationFragment = GiftResevationFragment().apply {
                arguments = bundle
            }

            parentFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment, giftReservationFragment, "giftreserve")
                .addToBackStack(null)
                .commitAllowingStateLoss()
        }

        return binding.root
    }

    private fun initTab() {
        val mainVPAdapter = ExDetailVPAdapter(requireActivity(), arguments)

        binding.vpMain.adapter = mainVPAdapter

        val tabTitleArray = arrayOf("설명", "안내")

        TabLayoutMediator(binding.tabMain, binding.vpMain) { tab, position ->
            tab.text = tabTitleArray[position]
        }.attach()
    }

    inner class ImageAdapter(private val context: Context, private val imageUrls: List<String?>) : RecyclerView.Adapter<ImageAdapter.ViewHolder>() {
        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val imageView: ImageView = view.findViewById(R.id.exdetail_image)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false)
            return ViewHolder(view)
        }

        override fun getItemCount() = imageUrls.size

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            Glide.with(context)
                .load(imageUrls[position])
                .into(holder.imageView)
        }
    }


    inner class ExDetailVPAdapter(private val fragmentActivity: FragmentActivity, private val bundle: Bundle?) : FragmentStateAdapter(fragmentActivity) {
        override fun getItemCount() = 2

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> ExDetailFragment().apply { arguments = this@ExDetailVPAdapter.bundle }
                1 -> ExDetailPresentFragment().apply { arguments = this@ExDetailVPAdapter.bundle }
                else -> throw IllegalArgumentException("Invalid position: $position")
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val supportActionBar = (requireActivity() as AppCompatActivity).supportActionBar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}