package com.shall_we.login

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.shall_we.R
import com.shall_we.databinding.FragmentPhoneAuthBinding
import com.shall_we.retrofit.RESPONSE_STATE
import com.shall_we.retrofit.RetrofitManager
import com.shall_we.utils.initProductRecycler

class PhoneAuthFragment : Fragment() {
    private lateinit var timerTv: TextView

    private var timer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentPhoneAuthBinding.inflate(inflater,container,false)
        timerTv = binding.timer
        binding.authSend.setOnClickListener {
            val phoneNumber = binding.phonenumberEt.text.toString()
            RetrofitManager.instance.sendOne(phoneNumber, completion = {
                responseState, responseBody ->
                    when(responseState){
                        RESPONSE_STATE.OKAY -> {
                            Log.d("retrofit", "category api : ${responseBody}")
                        }
                        RESPONSE_STATE.FAIL -> {
                            Log.d("retrofit", "api 호출 에러")
                        }
                    }
            })
            timerTv.visibility = View.VISIBLE
            startTimer()
        }
        binding.nextBtn.setOnClickListener {
            // 인증번호 검증 -> 번호 맞을때만 다음 프래그먼트로 넘기기
//            val newFragment = kakaoLoginFragment() // 전환할 다른 프래그먼트 객체 생성
//            val bundle = Bundle()
//            newFragment.arguments = bundle
//
//            // 프래그먼트 전환
//            parentFragmentManager.beginTransaction()
//                .replace(R.id.fragmentContainerView3, newFragment)
//                .addToBackStack(null)
//                .commit()
        }
        return binding.root
    }

    private fun startTimer() {
        timer?.cancel() // 이미 실행 중인 타이머가 있으면 취소합니다.

        timer = object : CountDownTimer(3 * 60 * 1000L, 1000) { // 3분 타이머 생성 (3분 * 60초 * 1000밀리초)
            override fun onTick(millisUntilFinished: Long) {
                val minutes = millisUntilFinished / (60 * 1000)
                val seconds = (millisUntilFinished % (60 * 1000)) / 1000
                timerTv.text = String.format("%02d:%02d", minutes, seconds) // 03:00 형식으로 표시합니다.
            }

            override fun onFinish() {
                timerTv.text = "00:00" // 타이머가 끝나면 00:00으로 표시합니다.
            }
        }.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel() // 액티비티가 종료될 때 타이머를 취소합니다.
    }


}