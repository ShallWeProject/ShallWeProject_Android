package com.shall_we.changeReservation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener
import com.prolificinteractive.materialcalendarview.format.TitleFormatter
import com.shall_we.R
import com.shall_we.base.BaseFragment
import com.shall_we.databinding.FragmentChangeReservationBinding
import com.shall_we.giftExperience.GiftExperienceFragment
import java.text.SimpleDateFormat
import java.util.Locale


class ChangeReservationFragment : BaseFragment<FragmentChangeReservationBinding>(R.layout.fragment_change_reservation) {
    private lateinit var calendarView: MaterialCalendarView
    private val locale: Locale = Locale("ko")
    override fun init() {
        val tabs = requireActivity().findViewById<View>(R.id.tabs)
        tabs.visibility = View.GONE
        calendarView = binding.calendar

        var isButtonSelected = false


        calendarView.setOnDateChangedListener(object : OnDateSelectedListener {
            override fun onDateSelected(
                widget: MaterialCalendarView,
                date: CalendarDay,
                selected: Boolean
            ) {
                if (selected) {
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
        binding.btnbtnbtn.setOnClickListener {
            val customDialog = CustomAlertDialog(requireContext())

            customDialog.setTitle("예약변경")
            customDialog.setMessage("9일 11시")

            val alertDialog = customDialog.create()
            alertDialog.show()

            customDialog.setPositiveButton("변경하기", View.OnClickListener {
                // 프래그먼트 이동 로직 추가
                binding.btnbtn.visibility=View.GONE
                binding.btnbtnbtn.visibility=View.GONE
                val transaction = requireActivity().supportFragmentManager.beginTransaction()
                 transaction.replace(R.id.change_reservation_layout, ChangedReservationFragment())
                transaction.addToBackStack(null)
                transaction.commit()

                alertDialog.dismiss()
            })


        }
    }


}