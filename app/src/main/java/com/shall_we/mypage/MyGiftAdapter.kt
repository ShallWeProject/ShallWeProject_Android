package com.shall_we.mypage

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.FragmentManager

import androidx.recyclerview.widget.RecyclerView
import com.shall_we.R
import com.shall_we.databinding.ItemGiftboxBinding
import com.shall_we.giftExperience.GiftExperienceFragment


class MyGiftAdapter(private val context: Context, private val parentFragmentManager: FragmentManager) : RecyclerView.Adapter<MyGiftAdapter.ViewHolder>(){
    var datas = mutableListOf<MyGiftData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemGiftboxBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = datas[position]
        holder.binding.tvDate.text = data.date
        holder.binding.tvTime.text = data.time
        holder.binding.tvTitle.text = data.title
        holder.binding.tvDescription.text = data.description
        holder.binding.tvMessage.text = data.message
        holder.binding.tvDate.setCompoundDrawablesRelativeWithIntrinsicBounds(
            holder.itemView.context.getDrawable(R.drawable.calendar_black_resize),
            null,
            null,
            null
        )
        holder.binding.tvTime.setCompoundDrawablesRelativeWithIntrinsicBounds(
            holder.itemView.context.getDrawable(R.drawable.time_black_resize),
            null,
            null,
            null
        )
        if (data.cancellation == false) {
            holder.binding.tvCancelReserv.visibility = View.GONE
            holder.binding.tvChangeReserv.visibility = View.GONE
        }

        if (holder.binding.tvMessage.visibility == View.VISIBLE) {
            changeColorExpanded(holder, position)
        } else {
            changeColorContacted(holder, position)
        }

        // 클릭 이벤트 처리
        holder.binding.cardView.setOnClickListener {
            if (holder.binding.tvMessage.visibility == View.VISIBLE) {
                changeColorContacted(holder, position)
            } else {
                changeColorExpanded(holder, position)
            }
        }
    }

    // 카드뷰 축소시 색상, visibility 변경
    private fun changeColorContacted(holder: ViewHolder, position: Int) {
        val data = datas[position]
        holder.binding.tvMessage.visibility = View.GONE
        holder.binding.tvChangeReserv.visibility = View.GONE
        holder.binding.tvCancelReserv.visibility = View.GONE
        holder.binding.constView.setBackgroundColor(Color.parseColor("#F8F8F8"))
        holder.binding.tvDate.setCompoundDrawablesRelativeWithIntrinsicBounds(
            holder.itemView.context.getDrawable(R.drawable.calendar_black_resize),
            null,
            null,
            null
        )
        holder.binding.tvTime.setCompoundDrawablesRelativeWithIntrinsicBounds(
            holder.itemView.context.getDrawable(R.drawable.time_black_resize),
            null,
            null,
            null
        )
        holder.binding.tvDate.setBackgroundResource(R.drawable.tv_date_unselected)
        holder.binding.tvTime.setBackgroundResource(R.drawable.tv_date_unselected)
        holder.binding.tvDate.setTextColor(Color.parseColor("#333333"))
        holder.binding.tvTime.setTextColor(Color.parseColor("#333333"))
    }

    // 카드뷰 확장시 색상, visibility 변경
    private fun changeColorExpanded(holder: ViewHolder, position: Int) {
        val data = datas[position]
        holder.binding.tvMessage.visibility = View.VISIBLE
        if (data.cancellation == true) {
            holder.binding.tvChangeReserv.visibility = View.VISIBLE
            holder.binding.tvCancelReserv.visibility = View.VISIBLE
        }
        holder.binding.constView.setBackgroundColor(Color.parseColor("#FFF5F6"))
        holder.binding.tvDate.setCompoundDrawablesRelativeWithIntrinsicBounds(
            holder.itemView.context.getDrawable(R.drawable.calendar_light_resize), // 시작 부분 Drawable 설정
            null,     // 위쪽 Drawable 설정 (null이면 이전 설정 유지)
            null,     // 끝 부분 Drawable 설정 (null이면 이전 설정 유지)
            null      // 아래쪽 Drawable 설정 (null이면 이전 설정 유지)
        )
        holder.binding.tvTime.setCompoundDrawablesRelativeWithIntrinsicBounds(
            holder.itemView.context.getDrawable(R.drawable.time_light_resize), // 시작 부분 Drawable 설정
            null,     // 위쪽 Drawable 설정 (null이면 이전 설정 유지)
            null,     // 끝 부분 Drawable 설정 (null이면 이전 설정 유지)
            null      // 아래쪽 Drawable 설정 (null이면 이전 설정 유지)
        )
        holder.binding.tvDate.setBackgroundResource(R.drawable.tv_date_selected)
        holder.binding.tvTime.setBackgroundResource(R.drawable.tv_date_selected)
        holder.binding.tvDate.setTextColor(Color.parseColor("#E31B54"))
        holder.binding.tvTime.setTextColor(Color.parseColor("#E31B54"))
    }


    override fun getItemCount(): Int {
        return datas.size
    }

    inner class ViewHolder(val binding: ItemGiftboxBinding) : RecyclerView.ViewHolder(binding.root){
        init {
            // 예약 변경으로 이동
            binding.tvChangeReserv.setOnClickListener {
                val position = adapterPosition
                val itemData = datas[position]
                val giftExperienceFragment = GiftExperienceFragment() // 전환할 프래그먼트 인스턴스 생성
                val fragmentTransaction = parentFragmentManager.beginTransaction()
                // 기존 프래그먼트를 숨기고 새로운 프래그먼트로 교체
                fragmentTransaction.replace(R.id.mypage_layout,giftExperienceFragment, "mypage")
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commitAllowingStateLoss()




//                // 클릭한 아이템 데이터 처리 (예: 다른 화면으로 이동 등)
//                // 특정 프래그먼트로 이동하려면 findNavController()를 사용하여 목적지로 이동
//                // 예를 들어, 목적지의 id가 myAlbumFragment인 경우:
//                val action = MyGiftReceivedFragmentDirections.actionMyGiftReceivedFragmentToMyAlbumFragment()
//                findNavController().navigate(action)

//                val myGiftReceivedFragment = MyGiftReceivedFragment()
//                val fragmentTransaction = parentFragmentManager.beginTransaction()
//                fragmentTransaction.replace(R.id.mypage_layout, myAlbumFragment, "myAlbumFragment")
//                fragmentTransaction.addToBackStack(null)
//                fragmentTransaction.commitAllowingStateLoss()
//                Log.d("clicked","change")

//                val newFragment = ChangeReservationFragment.newInstance("", "")
//                val fragmentTransaction = parentFragmentManager.beginTransaction()
//                fragmentTransaction.replace(R.id.myalbum, newFragment, "changeReservationFragment")
//                fragmentTransaction.addToBackStack(null)
//                fragmentTransaction.commitAllowingStateLoss()
            }

            // 예약 취소로 이동
            binding.tvCancelReserv.setOnClickListener {
                cuDialog(it, position)
            }
        }
    }
    fun cuDialog(view: View, position: Int) {
        val myLayout = LayoutInflater.from(context).inflate(R.layout.dialog_cancel_reservation, null)
        val build = AlertDialog.Builder(view.context).apply {
            setView(myLayout)
        }
        val dialog = build.create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialog.show()

        myLayout.findViewById<Button>(R.id.btn_cancel_reservation).setOnClickListener {
            Toast.makeText(view.context, "예약이 취소되었습니다.", Toast.LENGTH_SHORT).show()
            datas.apply {
                removeAt(position)
            }
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, itemCount)
            dialog.dismiss()
        }
        myLayout.findViewById<Button>(R.id.btn_cancel).setOnClickListener {
            dialog.dismiss()
        }
    }
}

