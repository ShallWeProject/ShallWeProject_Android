package com.shall_we.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.shall_we.R

class CustomSpinnerAdapter(
    context: Context,
    private val data: MutableList<String>
) : ArrayAdapter<String>(context, 0, data) {
    // Add a variable to track whether the spinner is open or closed
    private var isSpinnerOpened = false

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getCustomView(position, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        isSpinnerOpened = true

        val view = LayoutInflater.from(context).inflate(
            if (position == 0) {
                // 첫 번째 아이템의 높이를 다르게 설정한 커스텀 레이아웃 사용
                R.layout.custom_spinner_dropdown_opened_first
            } else {
                // 기본 드롭다운 레이아웃 사용
                R.layout.custom_spinner_dropdown_opened
            },
            parent,
            false
        )

        val text = view.findViewById<TextView>(R.id.text)
        val item = data[position]


        text.text = item

//        val view2 = LayoutInflater.from(context).inflate(R.layout.custom_spinner_dropdown_closed, parent, false)
//
//
//        val icon1 = view2.findViewById<ImageView>(R.id.icon1)
//        icon1.visibility = View.GONE

        return view
    }


    private fun getCustomView(position: Int, parent: ViewGroup): View {
        val view = LayoutInflater.from(context).inflate(R.layout.custom_spinner_dropdown_closed, parent, false)

        val text = view.findViewById<TextView>(R.id.text)

        val item = data[position]
        text.text = item

//        // Set the icon and other customization here if needed
//        val icon1 = view.findViewById<ImageView>(R.id.icon1)
//        icon1.visibility = View.VISIBLE


        return view
    }
    fun isSpinnerOpen(): Boolean {
        return isSpinnerOpened
    }

    // Call this method when you want to reset the isSpinnerOpened flag to false
    fun resetSpinnerOpenState() {
        isSpinnerOpened = false
    }

}