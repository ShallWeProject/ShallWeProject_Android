package com.shall_we.giftExperience

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.core.graphics.createBitmap
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener
import com.prolificinteractive.materialcalendarview.format.TitleFormatter
import com.shall_we.ExperienceDetail.ExperienceDetailFragment
import com.shall_we.ExperienceDetail.ExperienceDetailViewModel
import com.shall_we.R
import com.shall_we.base.BaseFragment
import com.shall_we.changeReservation.CustomAlertDialog
import com.shall_we.databinding.FragmentGiftResevationBinding
import com.shall_we.dto.ExperienceReq
import com.shall_we.retrofit.RESPONSE_STATE
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class GiftResevationFragment : BaseFragment<FragmentGiftResevationBinding>(R.layout.fragment_gift_resevation) {
    private var count: Int = 2
    private lateinit var calendarView: MaterialCalendarView
    private var experienceGiftId:Int=1
    var selectedDate: Date? = null
    lateinit var experienceDetailViewModel: ExperienceDetailViewModel
    private val locale: Locale = Locale("ko")
    override fun init() {

        arguments?.let { // 아규먼트로부터 데이터를 불러옴

            experienceGiftId = it.getInt("id") // id 키로 giftid 값을 불러와 저장하게 됩니다.


        }

        experienceDetailViewModel = ViewModelProvider(this).get(ExperienceDetailViewModel::class.java)
        experienceDetailViewModel.get_experience_detail_data(experienceGiftId, completion = {
                responseState, responseBody ->
            when(responseState){
                RESPONSE_STATE.OKAY -> {


                    responseBody?.get(0)?.let { item ->
                        binding.exgiftText02.text=item.subtitle
                        binding.exgiftText04.text = item.title

                        //val explanationsText = item.explanation?.joinToString(separator = "\n") {
                        //    it.description.toString()
                        //}
                        //binding.exdetailText04.text =explanationsText
                        Glide.with(this)
                            .load(item.giftImageUrl)
                            .into(binding.samplePic)



                        }


                    }



                RESPONSE_STATE.FAIL -> {
                    Log.d("retrofit", "api 호출 에러")
                }
            }
        })



        binding.exgiftBtn01.setOnClickListener {
            count--
            binding.exgiftText06.text = count.toString()
        }

        binding.exgiftBtn02.setOnClickListener {
            count++
            binding.exgiftText06.text = count.toString()
        }

        calendarView = binding.calendar

        var isButtonSelected = false


        calendarView.setOnDateChangedListener(object : OnDateSelectedListener {
            override fun onDateSelected(
                widget: MaterialCalendarView,
                date: CalendarDay,
                selected: Boolean
            ) {
                if (selected) {
                    selectedDate = date.date // 선택된 날짜 저장

                    // 특정 날짜가 선택되면 버튼을 표시
                    binding.btnbtn.visibility = View.VISIBLE
                } else {
                    // 선택이 취소되면 버튼을 숨김
                    binding.btnbtn.visibility = View.INVISIBLE
                }
            }
        })

        calendarView.setTitleFormatter(object : TitleFormatter {
            override fun format(calendarDay: CalendarDay?): CharSequence {
                val monthFormat = SimpleDateFormat("MMMM yyyy", locale)
                return monthFormat.format(calendarDay?.date)
            }
        })

        binding.btnbtn.setOnClickListener {
            isButtonSelected = !isButtonSelected
            binding.btnbtn.isSelected = isButtonSelected
        }


        binding.exgiftBtn03.setOnClickListener {
            val giftExperienceFragment = GiftExperienceFragment() // 전환할 프래그먼트 인스턴스 생성
            val fragmentTransaction = parentFragmentManager.beginTransaction()
            val bundle = Bundle()
            bundle.putInt("id", experienceGiftId) // 클릭된 아이템의 이름을 "title" 키로 전달
            bundle.putInt("persons",count)
            if (selectedDate != null) { // 선택된 날짜가 있으면
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", locale) // 날짜 형식 지정
                val dateString = dateFormat.format(selectedDate) // 문자열로 변환
                bundle.putString("Date", dateString) // "Date" 키로 날짜 전달
            }


           // 전환할 프래그먼트 인스턴스 생성
            giftExperienceFragment.arguments = bundle
            // 기존 프래그먼트를 숨기고 새로운 프래그먼트로 교체
            binding.exgiftBtn01.visibility=View.GONE
            binding.exgiftBtn02.visibility=View.GONE
            binding.exgiftBtn03.visibility=View.GONE
            binding.btnbtn.visibility=View.GONE
            fragmentTransaction.replace(R.id.gift_reservation_layout, giftExperienceFragment, "giftreserve")

            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commitAllowingStateLoss()
        }

    }

}