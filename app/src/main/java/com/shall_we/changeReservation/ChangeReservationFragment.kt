package com.shall_we.changeReservation

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener
import com.prolificinteractive.materialcalendarview.format.ArrayWeekDayFormatter
import com.prolificinteractive.materialcalendarview.format.TitleFormatter
import com.shall_we.R
import com.shall_we.base.BaseFragment
import com.shall_we.databinding.FragmentChangeReservationBinding
import com.shall_we.dto.UpdateReservationReq
import com.shall_we.giftExperience.DayDecorator
import com.shall_we.giftExperience.ReservationTimeAdapter
import com.shall_we.giftExperience.ReservationTimeData
import com.shall_we.giftExperience.SelectedMonthDecorator
import com.shall_we.retrofit.RESPONSE_STATE
import com.shall_we.retrofit.RetrofitManager
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale


class ChangeReservationFragment : BaseFragment<FragmentChangeReservationBinding>(R.layout.fragment_change_reservation), ReservationTimeAdapter.OnItemClickListener {
    private lateinit var calendarView: MaterialCalendarView
    private val locale: Locale = Locale("ko")
    var selectedDate: String? = null
    var idx: Int = 1
    var localDateTimeString : String? = null
    var time : String? = null
    var reservationId : Int = 1

    lateinit var reservationTimeAdapter: ReservationTimeAdapter

    @RequiresApi(Build.VERSION_CODES.O)
    override fun init() {
        idx = arguments?.getInt("expId", 1)!!
        reservationId = arguments?.getInt("idx", 1)!!
        val title = arguments?.getString("title", "")
        val description = arguments?.getString("description", "")
        val date = arguments?.getString("date", "")
        time = arguments?.getString("time", "")
        Log.d("time",time.toString())

        binding.exgiftText02.text = description
        binding.exgiftText04.text = title

        val dateFormatInput = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
        val parsedDate: Date = dateFormatInput.parse(date) ?: Date()

// Format the parsed date into "yyyy-MM-dd" format
        val dateFormatOutput = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        selectedDate = dateFormatOutput.format(parsedDate)

        Log.d("date",selectedDate.toString())

        timeRetrofitCall(binding.rvChTime,selectedDate.toString())

        val tabs = requireActivity().findViewById<View>(R.id.tabs)
        tabs.visibility = View.GONE

        calendarView = binding.changeCalendar

        var dayDecorator = DayDecorator(requireContext())
        var selectedMonthDecorator = SelectedMonthDecorator(CalendarDay.today().month)

        calendarView.addDecorators(dayDecorator,selectedMonthDecorator)

        calendarView.setOnDateChangedListener(object : OnDateSelectedListener {
            override fun onDateSelected(
                widget: MaterialCalendarView,
                date: CalendarDay,
                selected: Boolean
            ) {
                if (selected) {
                    time = null
                    val year = date.year
                    val month = String.format("%02d", date.month) // 월을 두 자리 숫자로 변환하고 앞에 0을 채웁니다.
                    val day = String.format("%02d", date.day) // 일을 두 자리 숫자로 변환하고 앞에 0을 채웁니다.

                    val formattedDate = "$year-$month-$day" // yyyy-mm-dd 형식의 문자열 생성

                    println("Formatted Date: $formattedDate")
                    timeRetrofitCall(binding.rvChTime,formattedDate)
                    selectedDate = formattedDate
                } else {
                }
            }
        })

        calendarView.setOnMonthChangedListener { widget, date ->
            // 기존에 설정되어 있던 Decorators 초기화
            calendarView.removeDecorators()

            // Decorators 추가
            selectedMonthDecorator = SelectedMonthDecorator(date.month)
            calendarView.addDecorators(dayDecorator, selectedMonthDecorator)
        }

        calendarView.setTitleFormatter(object : TitleFormatter {
            override fun format(calendarDay: CalendarDay?): CharSequence {
                val monthYearFormatter = SimpleDateFormat("yyyy년 MMMM", locale)
                val year = calendarDay?.year ?: return "" // CalendarDay가 null이면 빈 문자열 반환
                val month = calendarDay.month
                val calendar = Calendar.getInstance().apply {
                    set(Calendar.YEAR, year)
                    set(Calendar.MONTH, month - 1) // Calendar의 월은 0부터 시작하므로 1을 빼줍니다.
                }
                return monthYearFormatter.format(calendar.time)
            }
        })

        // 요일을 한글로 보이게 설정 월..일 순서로 배치해서 캘린더에는 일..월 순서로 보이도록 설정
        calendarView.setWeekDayFormatter(ArrayWeekDayFormatter(resources.getTextArray(R.array.custom_weekdays)))

        // 좌우 화살표 사이 연, 월의 폰트 스타일 설정
        calendarView.setHeaderTextAppearance(R.style.CalendarWidgetHeader)

        val inputFormat = DateTimeFormatter.ofPattern("yyyy.MM.dd")

        val localDate = LocalDate.parse(date, inputFormat)

        val year = localDate.year // 년
        val month = localDate.monthValue // 월 (1 ~ 12)
        val day = localDate.dayOfMonth // 일

        val calendarDay: CalendarDay = CalendarDay.from(year, month, day)

// Set the selected date on the MaterialCalendarView
        calendarView.selectedDate = calendarDay
        calendarView.setDateSelected(calendarDay, true)

        binding.btnChange.setOnClickListener {
            if (selectedDate != null) { // 선택된 날짜가 있으면
                val month = selectedDate.toString().substring(5,7) // 1을 더하는 이유는 0부터 시작하기 때문입니다.
                val day = selectedDate.toString().substring(8,10)


// LocalDateTime 객체를 문자열로 변환합니다. (원하는 포맷으로 지정)

                val customDialog = CustomAlertDialog(requireContext())

                customDialog.setTitle("예약변경")
                val simpleTime = time.toString().substring(0,2)
                customDialog.setMessage("${month}월 ${day}일 ${simpleTime}시")

                val alertDialog = customDialog.create()
                alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                alertDialog.show()


                customDialog.setPositiveButton("변경하기", View.OnClickListener {

                    RetrofitCall()

                    // 프래그먼트 이동 로직 추가
                    val transaction = requireActivity().supportFragmentManager.beginTransaction()
                    transaction.replace(
                        R.id.mypage_layout,
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
        val updateReservationReq = UpdateReservationReq(reservationId, selectedDate.toString(),time.toString())
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

    fun timeRetrofitCall(rvCategory: RecyclerView, date : String) {
        RetrofitManager.instance.get_reservation_date(ExperienceGiftId=idx,date= date, completion = { // 카테고리별 경험으로 바꾸기
                responseState, responseBody ->
            when(responseState){
                RESPONSE_STATE.OKAY -> {
                    var times = mutableListOf<ReservationTimeData>()
                    if(responseBody != null){
                        if(time != null){
                            times.add(ReservationTimeData(time = time.toString().substring(0, 2)))
                        }
                        for(i in 0 until responseBody.size){
                            if(responseBody.get(i).status == "WAITING"){
                                Log.d("time",responseBody.get(i).time)
                                times.add(ReservationTimeData(time = responseBody.get(i).time.toString().substring(0, 2)))
                            }
                        }
                    }
                    else{
                    }
                    times.sortBy { it.time }
                    initRecycler(rvCategory,times)
                }
                RESPONSE_STATE.FAIL -> {
                    Log.d("retrofit", "api 호출 에러")
                }
            }
        })
    }

    private fun initRecycler(rvCategory: RecyclerView, time:MutableList<ReservationTimeData>) {
        reservationTimeAdapter = ReservationTimeAdapter(requireContext())
        reservationTimeAdapter.setOnItemClickListener(this)

        rvCategory.adapter = reservationTimeAdapter


        reservationTimeAdapter.datas = time
        reservationTimeAdapter.notifyDataSetChanged()

    }

    override fun onItemClick(position: Int, text: String) {
        Log.d("time",text)
        time = "${text}:00"
    }

    override fun onStart() {
        super.onStart()
        val fab = requireActivity().findViewById<Button>(R.id.fab_album)
        fab.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        val tab = requireActivity().findViewById<TabLayout>(R.id.tabs)
        tab.visibility = View.VISIBLE
        val fab = requireActivity().findViewById<Button>(R.id.fab_album)
        fab.visibility = View.VISIBLE
    }
}



