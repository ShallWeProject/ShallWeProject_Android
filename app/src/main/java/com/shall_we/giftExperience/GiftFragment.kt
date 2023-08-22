package com.shall_we.giftExperience

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.createBitmap
import androidx.lifecycle.ViewModelProvider
import com.shall_we.ExperienceDetail.ExperienceDetailViewModel
import com.shall_we.mypage.MypageFragment
import com.shall_we.R
import com.shall_we.base.BaseFragment
import com.shall_we.databinding.FragmentExperienceDetailBinding
import com.shall_we.databinding.FragmentGiftBinding
import com.shall_we.dto.ReservationRequest
import com.shall_we.dto.ReservationStatus
import com.shall_we.home.HomeFragment
import com.shall_we.mypage.MyGiftReceivedFragment
import com.shall_we.mypage.MyGiftSentFragment
import com.shall_we.retrofit.RESPONSE_STATE
import com.shall_we.search.SearchFragment
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Date
import java.util.Locale


class GiftFragment : BaseFragment<FragmentGiftBinding>(R.layout.fragment_gift) {

    lateinit var experienceDetailViewModel: ExperienceDetailViewModel
    lateinit var reservationViewModel:ReservationViewModel
    lateinit var reservationRequest: ReservationRequest
    private var experienceGiftId:Int=1
    private var persons:Int=2
    private var date: String? = null
    private var dated:Date?=null
    private var receiverName:String="땡땡땡"
    private var phoneNumber:String="01000000000"
    private var imageKey:String="?"
    lateinit var localDateTime:String
    private var invitationComment: String="환영해!"
    private var reservationStatus: ReservationStatus = ReservationStatus.BOOKED


    override fun init() {

        arguments?.let { // 아규먼트로부터 데이터를 가져옴
            experienceGiftId = it.getInt("id")
            persons = it.getInt("persons")
            receiverName = it.getString("receivername").toString()
            invitationComment = it.getString("invitationComment").toString()
            date=it.getString("date")
            }

//        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
//        val localDate = LocalDate.parse(date, formatter)
//         localDateTime = localDate.atStartOfDay()
//        Log.d("date",localDateTime.toString())
//        localDateTime = LocalDateTime.now()
//        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
//        val formattedDateTime = localDateTime.format(formatter)
        val zonedDateTime = ZonedDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        localDateTime = zonedDateTime.format(formatter)

            reservationRequest = ReservationRequest(
                experienceGiftId = experienceGiftId,
                persons = persons,
                date = localDateTime,
                receiverName = receiverName,
                phoneNumber = "01074167048",
                imageKey = imageKey,
                invitationComment = invitationComment,
                reservationStatus = "BOOKED"
            )

//        reservationRequest= ReservationRequest(
//            experienceGiftId=16,
//            persons=3,
//            date="33",
//            receiverName="우쪁",
//            phoneNumber="01074167048",
//            imageKey="ssss",
//            invitationComment="생일축하!",
//            reservationStatus="BOOKED"
//
//
//        )
            Log.d("reservationrequest", reservationRequest.toString())
            experienceDetailViewModel =
                ViewModelProvider(this).get(ExperienceDetailViewModel::class.java)
            reservationViewModel = ViewModelProvider(this).get(ReservationViewModel::class.java)
            reservationViewModel.set_experience_gift(reservationRequest)
            binding.giftBtn01.setOnClickListener()
            {

                val homeFragment = HomeFragment() // 전환할 프래그먼트 인스턴스 생성
                val fragmentTransaction = parentFragmentManager.beginTransaction()
                // 기존 프래그먼트를 숨기고 새로운 프래그먼트로 교체
                fragmentTransaction.replace(R.id.home_layout, homeFragment, "home")

                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commitAllowingStateLoss()

            }
//        binding.giftBtn02.setOnClickListener()
//        {
//            Log.d("btn","clicked")
//            binding.giftBtn02.visibility=View.GONE
//            binding.giftBtn01.visibility=View.GONE
//            val myPageFragment = MypageFragment() // 전환할 프래그먼트 인스턴스 생성
//            val fragmentTransaction = parentFragmentManager.beginTransaction()
//            // 기존 프래그먼트를 숨기고 새로운 프래그먼트로 교체
//            fragmentTransaction.replace(R.id.giftLayout, myPageFragment, "mypage")
//
//            fragmentTransaction.addToBackStack(null)
//            fragmentTransaction.commitAllowingStateLoss()
//
//        }

        }


    }
