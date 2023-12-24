package com.shall_we.giftExperience

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.shall_we.ExperienceDetail.ExperienceDetailViewModel
import com.shall_we.R
import com.shall_we.databinding.FragmentGiftExperienceBinding
import com.shall_we.dto.ReservationRequest
import com.shall_we.dto.ReservationStatus


class GiftExperienceFragment : Fragment() {

  lateinit var experienceDetailViewModel: ExperienceDetailViewModel
    lateinit var reservationViewModel:ReservationViewModel
    lateinit var reservationRequest: ReservationRequest

    private var experienceGiftId:Int=1
    private var persons:Int=2
    var selectedDate: String? = null
    var selectedTime: String? = null
    private lateinit var binding: FragmentGiftExperienceBinding
    private var receiverName:String="땡땡땡"
    private var senderName:String="땡땡땡떙"
    private var phonenum1:String="010"
    private var phonenum2:String="000"
    private var phonenum3:String="000"
    private var imageKey:String="?"
    private var invitationComment: String="환영해!"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGiftExperienceBinding.inflate(inflater, container, false)  // Binding 객체 초기화

        arguments?.let {


            experienceGiftId = it.getInt("id") // id 키로 giftid 값을 불러와 저장하게 됩니다.
            persons=it.getInt("persons")
            selectedDate=it.getString("Date")
            selectedTime = it.getString("time")!!
        }

        //보내는사람 이름
                binding.etSenderName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                setEditTextBackground(binding.etSenderName, s)
                senderName=s.toString()
                // 수정된 부분
            }
        })
        //받는 사람 이름
        binding.etReceiverName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
            override fun afterTextChanged(s: Editable?) {
                setEditTextBackground(binding.etReceiverName, s)
                Log.d("receivername",receiverName)
                receiverName = s.toString()
            }
        })
        binding.etInvitationComment.bringToFront()
        //초대장 내용
        binding.etInvitationComment.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                invitationComment = s.toString()
                var currentLineCount = 0

                val text = s.toString()
                val lines = text.split("\n")
                currentLineCount = binding.etInvitationComment.lineCount

                // 최대 줄 수를 초과하는 경우 입력을 막음
                if (currentLineCount > 5) {
                    binding.etInvitationComment.isEnabled = false
                    binding.etInvitationComment.isFocusable = false
                    binding.etInvitationComment.isFocusableInTouchMode = false
                    Toast.makeText(requireContext(), "최대 5줄까지 입력 가능합니다.", Toast.LENGTH_SHORT).show()
                }
                binding.etInvitationComment.isEnabled = true
                binding.etInvitationComment.isFocusable = true
                binding.etInvitationComment.isFocusableInTouchMode = true
            }
        })

        //번호2
        binding.etSecNum.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                setEditTextBackground(binding.etSecNum, s)
                phonenum2=s.toString()
            }
        })

        //번호3
        binding.etLastNum.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                setEditTextBackground(binding.etLastNum, s)
                phonenum3=s.toString()
            }
        })

//        // 스피너 어댑터 생성
//        val spinneradapter =
//            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, numdata)
//
//        // 스피너 드롭다운 레이아웃 설정
//        spinneradapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // 모든 EditText들을 담는 리스트
        val editTextList = listOf(
            binding.etSenderName,
            binding.etReceiverName,
            binding.etInvitationComment,
            binding.etSecNum,
            binding.etLastNum
        )

        // EditText들의 텍스트가 변경될 때마다 호출되는 리스너
        val editTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                // 모든 EditText들이 채워져 있는지 확인
                val allFilled = editTextList.all { it.text.toString().isNotEmpty() }

                // 버튼 디자인 변경
                if (allFilled) {
                    binding.giftreserveBtn02.setBackgroundResource(R.drawable.gift_btn)
                    binding.giftreserveBtn02.isClickable = true
                } else {
                    binding.giftreserveBtn02.setBackgroundResource(R.drawable.gift_btn_black)
                    binding.giftreserveBtn02.isClickable=false
                }
            }
        }

        // 모든 EditText들에 리스너 추가
        editTextList.forEach { it.addTextChangedListener(editTextWatcher) }


        binding.giftreserveBtn02.setOnClickListener(){
            binding.giftreserveBtn02.visibility = View.GONE
           // binding.exgiftBtn02.visibility=View.GONE

            binding.etInvitationComment.visibility=View.GONE

            binding.giftreserveBtn02.visibility=View.GONE
            //val phoneNum = String.format("%s-%s-%s", phonenum1, phonenum2, phonenum3)
            val phoneNum = phonenum1+phonenum2+phonenum3
            reservationRequest = ReservationRequest(
                experienceGiftId = experienceGiftId,
                persons = persons,
                date = selectedDate!!,
                time = selectedTime!!,
                phoneNumber = phoneNum,
                imageKey = imageKey,
                invitationComment = invitationComment
            )

            Log.d("reservationrequest", reservationRequest.toString())
            experienceDetailViewModel =
                ViewModelProvider(this).get(ExperienceDetailViewModel::class.java)
            reservationViewModel = ViewModelProvider(this).get(ReservationViewModel::class.java)
            reservationViewModel.set_experience_gift(reservationRequest)


            val giftFragment = GiftFragment() // 전환할 프래그먼트 인스턴스 생성
            val bundle = Bundle()
            giftFragment.arguments = bundle

            val fragmentTransaction = parentFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.nav_host_fragment, giftFragment, "gift")

            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commitAllowingStateLoss()
        }
        return binding.root
    }

    private fun setEditTextBackground(editText: EditText, s: Editable?) {
        if (s.isNullOrEmpty()) {
            editText.setBackgroundResource(R.drawable.edittext_design02)
        } else {
            editText.setBackgroundResource(R.drawable.edittext_design)
        }
    }


}
