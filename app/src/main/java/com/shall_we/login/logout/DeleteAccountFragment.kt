package com.shall_we.login.logout

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.Spinner
import com.shall_we.R
import com.shall_we.databinding.FragmentDeleteAccountBinding
import com.shall_we.home.CustomSpinnerAdapter
import com.shall_we.utils.dpToPx

class DeleteAccountFragment : Fragment() {
    lateinit var spinnerString : String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentDeleteAccountBinding.inflate(inflater,container,false)

        binding.spinner2.dropDownVerticalOffset = dpToPx(6) // spinner와 dropdown 간
        initSpinner(binding.spinner2)

        binding.btnDelete.setOnClickListener {
            cuDialog(it)
        }
        return binding.root
    }

    private fun initSpinner(spinner : Spinner) {
        //spinner

        val spinnerItems = arrayOf("자주 사용하지 않아요.",
            "중복 계정이 있어요.",
            "앱 오류가 있어요.",
            "타사 대비 가격이 비싸요.",
            "상품(경험) 종류가 적어요.",
            "사용성/화면 디자인이 불편해요.",
            "기타 사유가 있어요.")
        val spinneradapter = DeleteSpinnerAdapter(requireContext(), spinnerItems)
        spinneradapter.setDropDownViewResource(R.layout.custom_delete_spinner)
        spinner.adapter = spinneradapter


        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                //아이템이 클릭 되면 맨 위부터 position 0번부터 순서대로 동작하게 됩니다.
                spinnerString = spinnerItems[position]
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }
    }

    private fun cuDialog(view: View) {
        val dialog = Dialog(view.context)
        dialog.setContentView(R.layout.dialog_delete_account)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialog.window?.setLayout(dpToPx(226), dpToPx(123))

        dialog.show()
        // 5초 후에 다이얼로그를 닫습니다.
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            dialog.dismiss()
        }, 5000) // 5초 (5000밀리초) 후에 다이얼로그를 닫음
    }
}

