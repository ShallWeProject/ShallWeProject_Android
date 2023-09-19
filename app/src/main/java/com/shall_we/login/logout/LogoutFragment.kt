package com.shall_we.login.logout

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.shall_we.App
import com.shall_we.R
import com.shall_we.databinding.FragmentLogoutBinding
import com.shall_we.login.data.AuthSignService
import com.shall_we.login.data.IAuthSign
import com.shall_we.retrofit.RESPONSE_STATE
import com.shall_we.retrofit.RefreshTokenArray
import com.shall_we.retrofit.RetrofitManager
import com.shall_we.utils.initProductRecycler

class LogoutFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentLogoutBinding.inflate(inflater,container,false)


        binding.logoutBtn.setOnClickListener {
            cuDialog(it)
        }
        return binding.root
    }

    private fun cuDialog(view: View) {
        val myLayout = LayoutInflater.from(context).inflate(R.layout.dialog_logout, null)
        val build = AlertDialog.Builder(view.context).apply {
            setView(myLayout)
        }
        val dialog = build.create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialog.show()

        myLayout.findViewById<Button>(R.id.btn_cancel_logout).setOnClickListener {
            // 로그아웃 api 연결
            logoutRetrofitCall()
            dialog.dismiss()

        }
        myLayout.findViewById<Button>(R.id.btn_logout).setOnClickListener {
            dialog.dismiss()
        }
    }

    fun logoutRetrofitCall() {
        val refreshTokenArray = RefreshTokenArray(App.refreshToken!!)
        RetrofitManager.instance.postSignOut(refreshTokenArray, completion = { responseState, responseBody ->
            when (responseState) {
                RESPONSE_STATE.OKAY -> {
                    Log.d("retrofit", "category api : ${responseBody}")
                    // 토큰 리셋
                    val sharedPref = context?.getSharedPreferences("MY_APP_PREFS", Context.MODE_PRIVATE)
                    sharedPref?.edit()?.putString("ACCESS_TOKEN", null)?.apply()
                    sharedPref?.edit()?.putString("REFRESH_TOKEN", null)?.apply()

                    App.accessToken = sharedPref?.getString("ACCESS_TOKEN", null)
                    App.refreshToken = sharedPref?.getString("REFRESH_TOKEN", null)
                    //로그인 화면으로 돌아가기
                    requireActivity().finish()
                }

                RESPONSE_STATE.FAIL -> {
                    Log.d("retrofit", "api 호출 에러")
                }
            }
        })

    }

}