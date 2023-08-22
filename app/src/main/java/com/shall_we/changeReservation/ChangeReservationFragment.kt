package com.shall_we.changeReservation

import android.database.Cursor
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.view.View
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener
import com.prolificinteractive.materialcalendarview.format.TitleFormatter
import com.shall_we.R
import com.shall_we.base.BaseFragment
import com.shall_we.databinding.FragmentChangeReservationBinding
import com.shall_we.dto.UpdateReservationReq
import com.shall_we.retrofit.RESPONSE_STATE
import com.shall_we.retrofit.RetrofitManager
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale


class ChangeReservationFragment : BaseFragment<FragmentChangeReservationBinding>(R.layout.fragment_change_reservation) {
    private lateinit var calendarView: MaterialCalendarView
    private val locale: Locale = Locale("ko")
    var selectedDate: Date? = null
    var idx: Int = 1
    var localDateTimeString : String? = null
    override fun init() {
        idx = arguments?.getInt("idx", 1)!!
        val title = arguments?.getString("title", "")
        val description = arguments?.getString("description", "")
        val date = arguments?.getString("date", "")

        binding.exgiftText02.text = description
        binding.exgiftText04.text = title

        val tabs = requireActivity().findViewById<View>(R.id.tabs)
        tabs.visibility = View.GONE
        calendarView = binding.calendar

        var isButtonSelected = false


        calendarView.setTitleFormatter(object : TitleFormatter {
            override fun format(calendarDay: CalendarDay?): CharSequence {
                val monthFormat = SimpleDateFormat("MMMM yyyy", locale)
                Log.d("date", "$monthFormat")
                return monthFormat.format(calendarDay?.date)
            }
        })

        calendarView.setOnDateChangedListener(object : OnDateSelectedListener {
            override fun onDateSelected(
                widget: MaterialCalendarView,
                date: CalendarDay,
                selected: Boolean
            ) {
                if (selected) {
                    selectedDate = date.date // 선택된 날짜 저장
                }
            }
        })


// 입력된 날짜 형식
        val inputFormat = DateTimeFormatter.ofPattern("yyyy.MM.dd")

// 날짜 문자열을 LocalDate 객체로 변환 (시각 정보가 없으므로 LocalDateTime으로 변환하지 않음)
        val localDate = LocalDate.parse(date, inputFormat)

        val year = localDate.year // 년
        val month = localDate.monthValue // 월 (1 ~ 12)
        val day = localDate.dayOfMonth // 일

// 시간 추출 - 기본 시각을 0으로 사용
        val hour = 0 // 시 (0 ~ 23)


        val calendar = Calendar.getInstance().apply {
            set(year, month - 1, day, 13, 0, 0)
            set(Calendar.MILLISECOND, 0)
        }


        val calendarDay = CalendarDay.from(calendar)
        calendarView.selectedDate = calendarDay
        calendarView.callOnClick()

        binding.btnbtn.setOnClickListener {
            isButtonSelected = !isButtonSelected
            binding.btnbtn.isSelected = isButtonSelected
        }
        binding.btnbtnbtn.setOnClickListener {
            if (selectedDate != null) { // 선택된 날짜가 있으면
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", locale) // 날짜 형식 지정
                val dateString = selectedDate?.let { it1 -> dateFormat.format(it1) } // 문자열로 변환

                val date = dateFormat.parse(dateString)
                // 월과 일 정보를 추출합니다.
                calendar.time = date
                val month = calendar.get(Calendar.MONTH) + 1 // 1을 더하는 이유는 0부터 시작하기 때문입니다.
                val day = calendar.get(Calendar.DAY_OF_MONTH)

                val localDateTime = selectedDate?.let {
                    LocalDateTime.ofInstant(it.toInstant(), ZoneId.systemDefault())
                }

// 선택된 날짜와 원하는 시각(13시)을 설정한 LocalDateTime 객체를 생성합니다.
                val targetDateTime = localDateTime?.withHour(13) ?: LocalDateTime.now()

// LocalDateTime 객체를 문자열로 변환합니다. (원하는 포맷으로 지정)
                val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
                localDateTimeString = targetDateTime.format(dateTimeFormatter)

                val customDialog = CustomAlertDialog(requireContext())

                customDialog.setTitle("예약변경")
                customDialog.setMessage("${month}월 ${day}일 13시")

                val alertDialog = customDialog.create()
                alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                alertDialog.show()


                customDialog.setPositiveButton("변경하기", View.OnClickListener {

                    RetrofitCall()

                    // 프래그먼트 이동 로직 추가
                    binding.btnbtn.visibility = View.GONE
                    binding.btnbtnbtn.visibility = View.GONE
                    val transaction = requireActivity().supportFragmentManager.beginTransaction()
                    transaction.replace(
                        R.id.change_reservation_layout,
                        ChangedReservationFragment()
                    )
                    transaction.addToBackStack(null)
                    transaction.commit()

                    alertDialog.dismiss()
                })

            customDialog.setNegativeButton("취소",View.OnClickListener {
                // 취소버튼 누르면 대화상자 종료
                alertDialog.dismiss()
            })


            }
        }
    }
    fun RetrofitCall(){
        Log.d("date","$localDateTimeString")
        val updateReservationReq = UpdateReservationReq(idx, localDateTimeString.toString())
        RetrofitManager.instance.putUpdateReservation(updateReservationReq = updateReservationReq,  completion = {
                responseState ->
            when(responseState){
                RESPONSE_STATE.OKAY -> {
                    Log.d("retrofit", "updateReservation")
                }
                RESPONSE_STATE.FAIL -> {
                    Log.d("retrofit", "api 호출 에러")
                }
            }
        })
    }

}



