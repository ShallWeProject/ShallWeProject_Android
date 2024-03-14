package com.shall_we.ExperienceDetail

import GiftDTO
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


class ExperienceDetailFragment: Fragment(){
    private lateinit var binding: FragmentExperienceDetailBinding
    lateinit var experienceDetailViewModel: ExperienceDetailViewModel
    lateinit var reservationViewModel: ReservationViewModel
    private var experienceGiftId: Int = 1

    private lateinit var experienceDetailRes: GiftDTO

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentExperienceDetailBinding.inflate(inflater, container, false)

        arguments?.let { // 아규먼트로부터 데이터를 불러옴
            experienceGiftId = it.getInt("id") // id 키로 giftid 값을 불러와 저장하게 됩니다.
        }

        experienceDetailViewModel = ViewModelProvider(this).get(ExperienceDetailViewModel::class.java)
        // reservationViewModel = ViewModelProvider(this).get(ReservationViewModel::class.java)

        experienceDetailViewModel.getExperienceDetailData(
            experienceGiftId,
            completion = { responseState, responseBody ->
                when (responseState) {
                    RESPONSE_STATE.OKAY -> {
                        responseBody?.let { item ->
                            experienceDetailRes = item
                            initTab()

                            binding.exdetailText01.text = item.subtitle
                            binding.exdetailText03.text = item.title
                            val formattedPrice = String.format("%,d", item.price.toInt())
                            binding.exdetailText04.text = formattedPrice.toString()


                            val giftImgUrlSize = item.giftImgUrl.size
                            // 여러 개의 이미지 URL을 사용하는 경우
                            val dummyImageUrls = mutableListOf<String>()

                            if(giftImgUrlSize == 0){
                                dummyImageUrls.add("")
                            }

                            // giftImgUrl 배열의 크기만큼 반복하여 dummyImageUrls에 이미지 URL을 추가합니다.
                            for (i in 0 until giftImgUrlSize) {
                                if (i < item.giftImgUrl.size) {
                                    dummyImageUrls.add(item.giftImgUrl[i])
                                } else {
                                    dummyImageUrls.add("") // 이 부분을 필요에 따라 수정해주세요.
                                }
                            }
                            // ViewPager의 Adapter 설정
                            val adapter = ImageAdapter(requireContext(), dummyImageUrls)
                            binding.exdetailImage.adapter = adapter
                        }
                    }
                    RESPONSE_STATE.FAIL -> {
                        Log.d("retrofit", "api 호출 에러")
                    }
                }
            }
        )

        binding.fab.setOnClickListener() {
            val bundle = Bundle().apply {
                putInt("id", experienceGiftId) // 클릭된 아이템의 이름을 "title" 키로 전달
                putParcelable("experienceDetailRes", experienceDetailRes)
            }

            val giftReservationFragment = GiftResevationFragment().apply {
                arguments = bundle
            }

            parentFragmentManager.beginTransaction()
                .add(R.id.nav_host_fragment, giftReservationFragment, "giftreserve")
                .addToBackStack(null)
                .commitAllowingStateLoss()
        }

        return binding.root
    }

    private fun initTab() {
        val bundle = Bundle()
        bundle.putParcelable("experienceDetailRes", experienceDetailRes)

        val mainVPAdapter = ExDetailVPAdapter(requireActivity(), bundle)

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
                0 -> ExDetailFragment().apply { arguments = this@ExDetailVPAdapter.bundle}
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