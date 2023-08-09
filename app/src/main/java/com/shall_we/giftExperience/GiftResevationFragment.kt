package com.shall_we.giftExperience

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.core.graphics.createBitmap
import com.shall_we.ExperienceDetail.ExperienceDetailFragment
import com.shall_we.R
import com.shall_we.base.BaseFragment
import com.shall_we.databinding.FragmentGiftResevationBinding


class GiftResevationFragment : BaseFragment<FragmentGiftResevationBinding>(R.layout.fragment_gift_resevation) {
    override fun init() {
        binding.giftreserveEdittext01.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                setEditTextBackground(binding.giftreserveEdittext01, s)
            }
        })

        binding.giftreserveEdittext02.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                setEditTextBackground(binding.giftreserveEdittext02, s)
            }
        })
        binding.giftreserveEdittext03.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {

            }
        })
        binding.giftreserveEdittext04.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                setEditTextBackground(binding.giftreserveEdittext04, s)
            }
        })
        binding.giftreserveEdittext05.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                setEditTextBackground(binding.giftreserveEdittext05, s)
            }
        })



        val numdata = listOf(" ", "010", "02", "031", "000")

        // 스피너 어댑터 생성
        val spinneradapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, numdata)

        // 스피너 드롭다운 레이아웃 설정
        spinneradapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // 스피너에 어댑터 설정
        binding.spinner.adapter = spinneradapter

        // 스피너 아이템 선택 리스너 설정
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                // 선택된 아이템 처리
                val selectedItem = numdata[position]
                binding.spinner.setBackgroundResource(R.drawable.edittext_design)
                Log.d("spinner", "Selected: $selectedItem")
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                binding.spinner.setBackgroundResource(R.drawable.edittext_design02)
            }
        }
        // 모든 EditText들을 담는 리스트
        val editTextList = listOf(
            binding.giftreserveEdittext01,
            binding.giftreserveEdittext02,
            binding.giftreserveEdittext03,
            binding.giftreserveEdittext04,
            binding.giftreserveEdittext05
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
                    binding.giftreserveBtn02.setBackgroundResource(R.drawable.btn_payment)
                    binding.giftreserveBtn02.isClickable = true
                } else {
                    binding.giftreserveBtn02.setBackgroundResource(R.drawable.btn_pay)
                    binding.giftreserveBtn02.isClickable=false
                }
            }
        }

        // 모든 EditText들에 리스너 추가
        editTextList.forEach { it.addTextChangedListener(editTextWatcher) }



        binding.giftreserveBtn02.setOnClickListener(){
           Log.d("clicked","clicked")
            binding.giftreserveBtn02.visibility = View.GONE
            binding.exgiftBtn01.visibility=View.GONE
            binding.exgiftBtn02.visibility=View.GONE

            binding.giftreserveEdittext03.visibility=View.GONE

            binding.giftreserveBtn02.visibility=View.GONE
            binding.giftreserveBtn01.visibility=View.GONE

            val giftFragment = GiftFragment() // 전환할 프래그먼트 인스턴스 생성
            val fragmentTransaction = parentFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.gift_reservation_layout,giftFragment, "gift")

            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commitAllowingStateLoss()
        }
    }

    private fun setEditTextBackground(editText: EditText, s: Editable?) {
        if (s.isNullOrEmpty()) {
            editText.setBackgroundResource(R.drawable.edittext_design02)
        } else {
            editText.setBackgroundResource(R.drawable.edittext_design)
        }
    }





}