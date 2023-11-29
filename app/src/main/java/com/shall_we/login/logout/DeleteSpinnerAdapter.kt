package com.shall_we.login.logout

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.shall_we.R

class DeleteSpinnerAdapter (
    context: Context,
    private val data: Array<String>
) : ArrayAdapter<String>(context, 0, data) {
    private var isSpinnerOpened = false

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getCustomView(position, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        isSpinnerOpened = true

        val view = LayoutInflater.from(context).inflate(R.layout.custom_delete_spinner, parent, false)

        val text = view.findViewById<TextView>(R.id.text)
        val spinnerDownBtn = view.findViewById<ImageView>(R.id.spinner_down_btn)

        val item = data[position]
        text.text = item

        // 드롭다운 메뉴에서는 spinner_down_btn을 숨김 처리
        spinnerDownBtn.visibility = View.GONE

        return view
    }

    private fun getCustomView(position: Int, parent: ViewGroup): View {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.custom_delete_spinner, parent, false)

        val text = view.findViewById<TextView>(R.id.text)
        val spinnerDownBtn = view.findViewById<ImageView>(R.id.spinner_down_btn)

        val item = data[position]
        text.text = item

        // 스피너에서는 spinner_down_btn을 뒤집어서 보여줌
        if (!isSpinnerOpened) {
            spinnerDownBtn.scaleY = -1f
        }
        else {
            spinnerDownBtn.scaleY = 1f // 드롭다운 메뉴가 닫혔을 때 원래대로 돌리기
        }

        return view
    }

    fun isSpinnerOpen(): Boolean {
        return isSpinnerOpened
    }

    fun resetSpinnerOpenState() {
        isSpinnerOpened = false
    }
}
