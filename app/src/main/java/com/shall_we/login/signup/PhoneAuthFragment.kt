package com.shall_we.login.signup

import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.shall_we.R
import com.shall_we.databinding.FragmentPhoneAuthBinding
import com.shall_we.retrofit.RESPONSE_STATE
import com.shall_we.retrofit.RetrofitManager
import com.shall_we.retrofit.SendOneArray
import com.shall_we.retrofit.ValidVerificationArray
import com.shall_we.signup.AgreementFragment

class PhoneAuthFragment : Fragment() {
    private lateinit var timerTv: TextView

    private var timer: CountDownTimer? = null

    private lateinit var phoneNumber : String
    private lateinit var verificationCode : String

    var nameFlag: Boolean = false
    private lateinit var nameTxt : String
    private lateinit var phoneNumberTxt : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onResume() {
        super.onResume()
        val supportActionBar = (requireActivity() as AppCompatActivity).supportActionBar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentPhoneAuthBinding.inflate(inflater,container,false)
        timerTv = binding.timer

        var maxLength = 6
        var filters = arrayOf<InputFilter>(InputFilter.LengthFilter(maxLength))
        binding.codeEt.filters = filters

        maxLength = 11
        filters = arrayOf<InputFilter>(InputFilter.LengthFilter(maxLength))
        binding.phonenumberEt.filters = filters


        binding.nameEt.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(
                s: CharSequence?, start: Int, before: Int, count: Int
            ) {
                // 문자열이 변경될 때마다 호출되는 메소드
                val length = s?.length ?: 0
                if (length>0){
                    nameFlag = true
                }
                else if (length==0){
                    nameFlag = false
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })
        binding.codeEt.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(
                s: CharSequence?, start: Int, before: Int, count: Int
            ) {
                // 문자열이 변경될 때마다 호출되는 메소드
                val length = s?.length ?: 0
                if (length==6 ){
                    binding.nextBtn.setBackgroundResource(R.drawable.btn_next)
                    verificationCode = binding.codeEt.text.toString()
                    binding.nextBtn.isClickable = true
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        binding.authSend.setOnClickListener {
            phoneNumber = binding.phonenumberEt.text.toString()
            if(phoneNumber.length == 11 && nameFlag){
                binding.nameEt.isEnabled = false
                binding.phonenumberEt.isEnabled = false

                nameTxt = binding.nameEt.text.toString()
                phoneNumberTxt = binding.phonenumberEt.text.toString()

                sendRetrofitCall()
                timerTv.visibility = View.VISIBLE
                startTimer()
            }else if(!nameFlag){
                Toast.makeText(context,"이름을 다시 입력해주세요.",Toast.LENGTH_LONG).show()
            }
            else{
                Toast.makeText(context,"번호를 다시 입력해주세요.",Toast.LENGTH_LONG).show()
            }

        }

        binding.nextBtn.setOnClickListener {
            // 인증번호 검증 -> 번호 맞을때만 다음 프래그먼트로 넘기기
            validRetrofitCall()
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
    private fun sendRetrofitCall() {
        Log.d("retrofit","$phoneNumber")
        val sendOneArray : SendOneArray = SendOneArray(phoneNumber)
        RetrofitManager.instance.sendOne(sendOneArray, completion = {
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
    }

    private fun validRetrofitCall() {
        Log.d("retrofit","$phoneNumber")
        Log.d("retrofit","$verificationCode")

        val validVerificationArray : ValidVerificationArray = ValidVerificationArray(verificationCode,phoneNumber)

        RetrofitManager.instance.validVerification(validVerificationArray = validVerificationArray, completion = {
                responseState, responseBody ->
            when(responseState){
                RESPONSE_STATE.OKAY -> {
                    Log.d("retrofit", "category api : ${responseBody.hashCode()}")
                    if(responseBody.hashCode() == 200){
                        val newFragment = AgreementFragment() // 전환할 다른 프래그먼트 객체 생성
                        val bundle = Bundle()
                        bundle.putString("phone",phoneNumber)
                        newFragment.arguments = bundle
                        // 프래그먼트 전환
                        parentFragmentManager.beginTransaction()
                            .replace(R.id.fragmentContainerView3, newFragment)
                            .addToBackStack(null)
                            .commit()
                    }
                    else if (responseBody == 400){
                        Toast.makeText(context,"인증번호를 확인해주세요.",Toast.LENGTH_LONG).show()
                    }

                }
                RESPONSE_STATE.FAIL -> {
                    Log.d("retrofit", "api 호출 에러")
                }
            }
        })
    }


}