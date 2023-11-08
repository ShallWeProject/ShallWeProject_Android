package com.shall_we.login.signup

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.shall_we.R
import com.shall_we.databinding.FragmentProfileBinding
import com.shall_we.retrofit.RESPONSE_STATE
import com.shall_we.retrofit.RetrofitManager

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding

    var phone: String = ""
    var age: Int = 0
    var gender: String = ""
    var marketingAgree: Boolean = false

    private fun initProfile() {

        fun checkRbChecked(): Boolean {
            if (binding.btnMan.isChecked || binding.btnWoman.isChecked || binding.btnNone.isChecked) {
                return true
            }
            return false
        }

        val editTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                changeBtnClickable(binding.edtAge.text.toString().isNotEmpty() && checkRbChecked())
            }
        }
        binding.edtAge.addTextChangedListener(editTextWatcher)

        binding.btnMan.setOnCheckedChangeListener() { button, ischecked ->
            if (binding.edtAge.text.toString().isNotEmpty())
                changeBtnClickable(true)
            else {
                changeBtnClickable(false)

            }
        }
        binding.btnWoman.setOnCheckedChangeListener() { button, ischecked ->
            if (binding.edtAge.text.toString().isNotEmpty())
                changeBtnClickable(true)
            else {
                changeBtnClickable(false)

            }
        }
        binding.btnNone.setOnCheckedChangeListener() { button, ischecked ->
            if (binding.edtAge.text.toString().isNotEmpty())
                changeBtnClickable(true)
            else {
                changeBtnClickable(false)

            }
        }

        binding.btnNextProfile.setOnClickListener {
            //Todo: 앞에서 번호 받아와서 업데이트
            phone = arguments?.getString("phone", "").toString()
            age = binding.edtAge.text.toString().toInt()
            gender = "UNKNOWN"
            if (binding.btnMan.isChecked) gender = "MALE"
            else if (binding.btnWoman.isChecked) gender = "FEMALE"
            else if (binding.btnNone.isChecked) gender = "UNKNOWN"

            val isChecked = arguments?.getBoolean("isChecked", false)  // 기본값은 false
            if (isChecked == true) marketingAgree = true
            else marketingAgree = false

            var userData : UserData = UserData(phone, marketingAgree, age, gender)
            Log.d("retrofit", "api 호출 성공 : ${userData}")

            usersPatchApiCall()
        }
    }
    private fun usersPatchApiCall(){
        //레트로핏 연결
        var userData : UserData = UserData(phone, marketingAgree, age, gender)

        RetrofitManager.instance.usersPatch(userData, completion = { responseState, responseCode ->
            when (responseState) {
                RESPONSE_STATE.OKAY -> {
                    Log.d("retrofit", "usersPatch api 호출 성공 : ${responseCode!!}")
                    val signupSuccessFragment = SignupSuccessFragment() // 전환할 프래그먼트 인스턴스 생성
                    val fragmentTransaction = parentFragmentManager.beginTransaction()
                    fragmentTransaction.replace(R.id.fragmentContainerView3, signupSuccessFragment, "signup_success")
                    fragmentTransaction.addToBackStack(null)
                    fragmentTransaction.commitAllowingStateLoss()

                }
                RESPONSE_STATE.FAIL -> {
                    Log.d("retrofit", "api 호출 에러")
                }
            }
        })

    }
    fun changeBtnClickable(boolean: Boolean){
        if (boolean) {
            binding.btnNextProfile.setBackgroundResource(R.drawable.btn_next)
            binding.btnNextProfile.isClickable = true
        }
        else {
            binding.btnNextProfile.setBackgroundResource(R.drawable.btn_next_black)
            binding.btnNextProfile.isClickable = false
        }
    }

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
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initProfile()
    }
}