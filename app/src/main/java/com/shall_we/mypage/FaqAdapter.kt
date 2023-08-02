package com.shall_we.mypage

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
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
        holder.binding.tvAnswer.text = data.answer

    }


    override fun getItemCount(): Int {
        return datas.size
    }

    inner class ViewHolder(val binding: ItemFaqBinding) : RecyclerView.ViewHolder(binding.root)
}