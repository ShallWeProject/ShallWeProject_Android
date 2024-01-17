package com.shall_we.giftExperience

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener
import com.prolificinteractive.materialcalendarview.format.ArrayWeekDayFormatter
import com.prolificinteractive.materialcalendarview.format.TitleFormatter
import com.shall_we.ExperienceDetail.ExperienceDetailViewModel
import com.shall_we.R
import com.shall_we.databinding.FragmentGiftResevationBinding
import com.shall_we.dto.LocalTime
import com.shall_we.retrofit.RESPONSE_STATE
import com.shall_we.retrofit.RetrofitManager
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale



class GiftResevationFragment : Fragment(), ReservationTimeAdapter.OnItemClickListener{
    private var count: Int = 2
    private lateinit var calendarView: MaterialCalendarView
    private var experienceGiftId: Int = 1
    var formattedDate: String? = null
    lateinit var experienceDetailViewModel: ExperienceDetailViewModel
    private val locale: Locale = Locale("ko")
    private lateinit var binding: FragmentGiftResevationBinding  // Binding 객체 추가

    lateinit var reservationTimeAdapter: ReservationTimeAdapter

    var time : String? = null
  
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            FragmentGiftResevationBinding.inflate(inflater, container, false)  // Binding 객체 초기화

        arguments?.let { // 아규먼트로부터 데이터를 불러옴
            experienceGiftId = it.getInt("id") // id 키로 giftid 값을 불러와 저장하게 됩니다.
        }

        experienceDetailViewModel =
            ViewModelProvider(this).get(ExperienceDetailViewModel::class.java)
        experienceDetailViewModel.get_experience_detail_data(
            experienceGiftId,
            completion = { responseState, responseBody ->
                when (responseState) {
                    RESPONSE_STATE.OKAY -> {
                        responseBody?.let { item ->
                            binding.exgiftText02.text = item.subtitle
                            binding.exgiftText04.text = item.title
                            val giftImgUrlSize = item.giftImgUrl.size
                            val formattedPrice = String.format("%,d", item.price.toInt())
                            binding.tvPrice.text = formattedPrice.toString()+" 원"

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
                            Glide.with(this)
                                .load(dummyImageUrls[0])
                                .into(binding.samplePic)

                        }
                    }

                    RESPONSE_STATE.FAIL -> {
                        Log.d("retrofit", "api 호출 에러")
                    }
                }
            })

        binding.exgiftBtn01.setOnClickListener {
            if(count>1){
                count--
                binding.exgiftText06.text = count.toString()
            }
        }

        binding.exgiftBtn02.setOnClickListener {
            count++
            binding.exgiftText06.text = count.toString()
        }

        calendarView = binding.calendar

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
                    binding.exgiftBtn03.isClickable = false
                    binding.exgiftBtn03.setBackgroundResource(R.drawable.gift_btn_black)

                    val year = date.year
                    val month = String.format("%02d", date.month) // 월을 두 자리 숫자로 변환하고 앞에 0을 채웁니다.
                    val day = String.format("%02d", date.day) // 일을 두 자리 숫자로 변환하고 앞에 0을 채웁니다.

                    formattedDate = "$year-$month-$day" // yyyy-mm-dd 형식의 문자열 생성

                    println("Formatted Date: $formattedDate")
                    retrofitCall(binding.rvTime, formattedDate!!)
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


        binding.exgiftBtn03.setOnClickListener {
            val giftExperienceFragment = GiftExperienceFragment() // 전환할 프래그먼트 인스턴스 생성
            val fragmentTransaction = parentFragmentManager.beginTransaction()
            val bundle = Bundle()
            bundle.putInt("id", experienceGiftId) // 클릭된 아이템의 이름을 "title" 키로 전달
            bundle.putInt("persons", count)
            if (formattedDate != null) { // 선택된 날짜가 있으면
                bundle.putString("Date", formattedDate) // "Date" 키로 날짜 전달
            }
            if (time==null){
                bundle.putString("time", "00:00:00")

            }else{
                bundle.putString("time", "${time.toString().substring(0, 2)}:00:00")

            }


            // 전환할 프래그먼트 인스턴스 생성
            giftExperienceFragment.arguments = bundle
            // 기존 프래그먼트를 숨기고 새로운 프래그먼트로 교체
            binding.exgiftBtn01.visibility = View.GONE
            binding.exgiftBtn02.visibility = View.GONE
            binding.exgiftBtn03.visibility = View.GONE
            fragmentTransaction.add(
                R.id.nav_host_fragment,
                giftExperienceFragment,
                "giftreserve"
            )

            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commitAllowingStateLoss()
        }

        return binding.root
    }


    private fun initRecycler(rvCategory: RecyclerView, time:MutableList<ReservationTimeData>) {
        reservationTimeAdapter = ReservationTimeAdapter(requireContext())
        reservationTimeAdapter.setOnItemClickListener(this)

        rvCategory.adapter = reservationTimeAdapter


        reservationTimeAdapter.datas = time
        reservationTimeAdapter.notifyDataSetChanged()

    }

    private fun retrofitCall(rvCategory: RecyclerView,date : String) {
        RetrofitManager.instance.get_reservation_date(ExperienceGiftId=experienceGiftId,date= date, completion = { // 카테고리별 경험으로 바꾸기
            responseState, responseBody ->
        when(responseState){
            RESPONSE_STATE.OKAY -> {
                var time = mutableListOf<ReservationTimeData>()
                if(responseBody != null){
                    for(i in 0 until responseBody.size){
                        if(responseBody.get(i).status == "WAITING"){
                            Log.d("time",responseBody.get(i).time)
                            time.add(ReservationTimeData(time = responseBody.get(i).time.toString().substring(0, 2)))
                        }
                    }
                }
                initRecycler(rvCategory,time)
            }
            RESPONSE_STATE.FAIL -> {
                Log.d("retrofit", "api 호출 에러")
            }
        }
    })
    }

    override fun onItemClick(position: Int, text: String) {
        Log.d("time",text)
        time = text
        binding.exgiftBtn03.isClickable = true
        binding.exgiftBtn03.setBackgroundResource(R.drawable.gift_btn)
    }

    override fun onDestroy() {
        super.onDestroy()
        val fab = requireActivity().findViewById<ExtendedFloatingActionButton>(R.id.fab)
        fab.visibility = View.VISIBLE
    }
}

/* 선택된 날짜의 background를 설정하는 클래스 */
class DayDecorator(context: Context) : DayViewDecorator {
    private val drawable = ContextCompat.getDrawable(context,R.drawable.calendar_selector)
    // true를 리턴 시 모든 요일에 내가 설정한 드로어블이 적용된다
    override fun shouldDecorate(day: CalendarDay): Boolean {
        return true
    }

    // 일자 선택 시 내가 정의한 드로어블이 적용되도록 한다
    override fun decorate(view: DayViewFacade) {
        view.setSelectionDrawable(drawable!!)
    }
}

/* 이번달에 속하지 않지만 캘린더에 보여지는 이전달/다음달의 일부 날짜를 설정하는 클래스 */
class SelectedMonthDecorator(val selectedMonth : Int) : DayViewDecorator {
    override fun shouldDecorate(day: CalendarDay): Boolean {
        return day.month != selectedMonth
    }
    override fun decorate(view: DayViewFacade) {
        view.addSpan(ForegroundColorSpan(Color.parseColor("#DBDBDB")))
    }
}