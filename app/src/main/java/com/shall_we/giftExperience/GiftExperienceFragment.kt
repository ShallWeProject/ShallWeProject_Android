package com.shall_we.giftExperience

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CalendarView
import android.widget.TextView
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener
import com.prolificinteractive.materialcalendarview.format.TitleFormatter
import com.shall_we.ExperienceDetail.ExperienceDetailFragment
import com.shall_we.R
import com.shall_we.base.BaseFragment
import com.shall_we.changeReservation.CustomAlertDialog
import com.shall_we.databinding.FragmentGiftExperienceBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class GiftExperienceFragment : BaseFragment<FragmentGiftExperienceBinding>(R.layout.fragment_gift_experience) {


    private lateinit var calendarView: MaterialCalendarView
    private val locale: Locale = Locale("ko")

    override fun init() {
        calendarView = binding.calendar

        var isButtonSelected = false
        val tabs = requireActivity().findViewById<View>(R.id.tabs)
        tabs.visibility = View.GONE


        binding.exgiftBtn03.setOnClickListener {
            val customDialog = CustomAlertDialog(requireContext())

            customDialog.setTitle("예약변경")
            customDialog.setMessage("9일 11시")

            val alertDialog = customDialog.create()
            alertDialog.show()
        }


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


    }
}
