package com.shall_we.mypage


import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.shall_we.databinding.DialogCancelReservationBinding
import com.shall_we.databinding.FragmentMyGiftReceivedBinding

class DialogCancelReservation(private val context : AppCompatActivity) {

    private lateinit var binding: DialogCancelReservationBinding
    private lateinit var giftBinding: FragmentMyGiftReceivedBinding
    private val dlg = Dialog(context)   //부모 액티비티의 context 가 들어감

    fun show(content : String) {
        binding = DialogCancelReservationBinding.inflate(context.layoutInflater)
        dlg.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE)   //타이틀바 제거
        dlg.setContentView(binding.root)     //다이얼로그에 사용할 xml 파일을 불러옴
        dlg.setCancelable(false)    //다이얼로그의 바깥 화면을 눌렀을 때 다이얼로그가 닫히지 않도록 함


//        binding.content.text = content //부모 액티비티에서 전달 받은 텍스트 세팅

        //ok 버튼 동작
        binding.btnCancelReservation.setOnClickListener {

            binding.tvCancelDescription.text = "취소되었습니다."

            dlg.dismiss()
        }

        //cancel 버튼 동작
        binding.btnCancel.setOnClickListener {
            binding.tvCancelDescription.text = "아무일도일어나지않은아니오버튼."

            dlg.dismiss()
        }

        dlg.show()
    }
}