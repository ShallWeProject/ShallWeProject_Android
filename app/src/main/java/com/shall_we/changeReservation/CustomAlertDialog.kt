package com.shall_we.changeReservation

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.databinding.DataBindingUtil
import com.shall_we.R
import com.shall_we.databinding.CustomAlertDialogBinding

class CustomAlertDialog(private val context: Context) {

    private val binding: CustomAlertDialogBinding = DataBindingUtil.inflate(
        LayoutInflater.from(context),
        R.layout.custom_alert_dialog, // 레이아웃 파일명을 사용자의 실제 레이아웃에 맞게 수정
        null,
        false
    )

    fun create(): Dialog {
        val dialog = Dialog(context)
        dialog.setContentView(binding.root)
        return dialog
    }


    fun setTitle(title: String) {
        binding.tvDialogTitle.text = title
    }

    fun setMessage(message: String) {
        binding.tvDialogMessage01.text = message
    }

    fun setPositiveButton(text: String, listener: View.OnClickListener) {
        binding.btnDialogPositive.text = text
        binding.btnDialogPositive.setOnClickListener(listener)
    }

    fun setNegativeButton(text: String, listener: View.OnClickListener) {
        binding.btnDialogNegative.text = text
        binding.btnDialogNegative.setOnClickListener(listener)
    }
}