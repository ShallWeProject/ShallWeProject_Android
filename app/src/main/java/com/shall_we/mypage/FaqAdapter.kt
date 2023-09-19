package com.shall_we.mypage

import android.content.Context
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.shall_we.R
import com.shall_we.login.logout.LogoutFragment
import com.shall_we.databinding.ItemFaqBinding


class FaqAdapter(private val context: Context) : RecyclerView.Adapter<FaqAdapter.ViewHolder>() {
    var datas = mutableListOf<FaqDto>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemFaqBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = datas[position]

        holder.binding.tvQuestion.text = data.question

        holder.binding.tvAnswer.movementMethod = LinkMovementMethod.getInstance()

        // 특정 글자를 다른 색상으로 변경하기 위해 SpannableStringBuilder 사용
        val questionText = data.answer
        val spannableText = SpannableStringBuilder(questionText)
        if (position == 3) {
            val startIndex = questionText.indexOf("예약변경")
            val endIndex = startIndex + "예약변경".length
            val color = ContextCompat.getColor(context, R.color.rose300) // 리소스에서 색상 가져오기
            spannableText.setSpan(ForegroundColorSpan(color), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }

        if (position == 8) {
            val startIndex = questionText.indexOf("계정 설정")
            val endIndex = startIndex + "계정 설정".length

            // 클릭 가능한 텍스트로 만들기
            spannableText.setSpan(object : ClickableSpan() {
                override fun onClick(widget: View) {
                    Log.d("click","계정설정")
                    val newFragment = LogoutFragment() // faq페이지로 이동
                    val bundle = Bundle()
                    newFragment.arguments = bundle
                    val fragmentManager = (context as FragmentActivity).supportFragmentManager.beginTransaction()
                    // 프래그먼트 전환
                    fragmentManager.add(R.id.nav_host_fragment, newFragment)
                        .addToBackStack(null)
                        .commit()
                }
            }, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        holder.binding.tvAnswer.text = spannableText
    }


    override fun getItemCount(): Int {
        return datas.size
    }

    inner class ViewHolder(val binding: ItemFaqBinding) : RecyclerView.ViewHolder(binding.root)
}