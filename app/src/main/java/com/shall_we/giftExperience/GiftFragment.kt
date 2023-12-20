package com.shall_we.giftExperience

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.shall_we.ExperienceDetail.ExperienceDetailViewModel
import com.shall_we.R
import com.shall_we.databinding.FragmentGiftBinding
import com.shall_we.dto.LocalTime
import com.shall_we.dto.ReservationRequest
import com.shall_we.dto.ReservationStatus
import com.shall_we.home.HomeFragment
import com.shall_we.mypage.MyGiftSentFragment


class GiftFragment : Fragment() {

    lateinit var experienceDetailViewModel: ExperienceDetailViewModel
//    lateinit var reservationRequest: ReservationRequest
    private var experienceGiftId:Int=1
    private var persons:Int=2
    private var date: String? = null
    private var time:LocalTime?=null

    private var selectedTime:String?=null
    private var receiverName:String="땡땡땡"
    private var phoneNum:String="01000000000"
    private var imageKey:String="?"
    private var localDateTime:String="2023-11-30"
    private var invitationComment: String="환영해!"
    private var reservationStatus: ReservationStatus = ReservationStatus.BOOKED
    private lateinit var binding: FragmentGiftBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGiftBinding.inflate(inflater, container, false)  // Binding 객체 초기화

//        arguments?.let { // 아규먼트로부터 데이터를 가져옴
//
//            experienceGiftId = it.getInt("id")
//            persons = it.getInt("persons")
//            receiverName = it.getString("receivername").toString()
//            invitationComment = it.getString("invitationComment").toString()
//            date=it.getString("date")
//            selectedTime = it.getParcelable<LocalTime>("time")!!
//            phoneNum=it.getString("phonenumber").toString()
//
//            Log.d("bundle",it.toString())
//
//            }

//        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
//        val localDate = LocalDate.parse(date, formatter)
//         localDateTime = localDate.atStartOfDay()
//        Log.d("date",localDateTime.toString())
//        localDateTime = LocalDateTime.now()
//        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
//        val formattedDateTime = localDateTime.format(formatter)
        //val zonedDateTime = ZonedDateTime.now()
       // val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        //localDateTime = zonedDateTime.format(formatter)

//            reservationRequest = ReservationRequest(
//                experienceGiftId = experienceGiftId,
//                persons = persons,
//                date = localDateTime,
//                //receiverName = receiverName,
//                phoneNumber = phoneNum,
//                imageKey = imageKey,
//                invitationComment = invitationComment,
//                time = selectedTime!!
//                //reservationStatus = "BOOKED"
//            )

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
//            Log.d("reservationrequest", reservationRequest.toString())
            experienceDetailViewModel =
                ViewModelProvider(this).get(ExperienceDetailViewModel::class.java)

            binding.giftBtn01.setOnClickListener()
            {

                val homeFragment = HomeFragment() // 전환할 프래그먼트 인스턴스 생성
                val fragmentTransaction = parentFragmentManager.beginTransaction()
                // 기존 프래그먼트를 숨기고 새로운 프래그먼트로 교체
                fragmentTransaction.replace(R.id.nav_host_fragment, homeFragment, "home")

                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commitAllowingStateLoss()

            }
        binding.giftBtn02.setOnClickListener()
        {
            Log.d("btn","clicked")
            binding.giftBtn02.visibility=View.GONE
            binding.giftBtn01.visibility=View.GONE
            val myGiftSentFragment = MyGiftSentFragment() // 전환할 프래그먼트 인스턴스 생성
            val fragmentTransaction = parentFragmentManager.beginTransaction()
            // 기존 프래그먼트를 숨기고 새로운 프래그먼트로 교체
            fragmentTransaction.replace(R.id.nav_host_fragment, myGiftSentFragment, "mypage")

            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commitAllowingStateLoss()

        }
        return binding.root
    }


    }
