package com.shall_we.login.logout

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.shall_we.App
import com.shall_we.MainActivity
import com.shall_we.R
import com.shall_we.databinding.FragmentLogoutBinding
import com.shall_we.login.LoginActivity
import com.shall_we.retrofit.RESPONSE_STATE
import com.shall_we.retrofit.RefreshTokenArray
import com.shall_we.retrofit.RetrofitManager

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

        binding.deleteBtn.setOnClickListener {
            val newFragment = DeleteAccountFragment() // 전환할 다른 프래그먼트 객체 생성
            val bundle = Bundle()
            newFragment.arguments = bundle

            // 프래그먼트 전환
            parentFragmentManager.beginTransaction()
                .add(R.id.nav_host_fragment, newFragment)
                .addToBackStack(null)
                .commit()
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

        myLayout.findViewById<Button>(R.id.btn_logout_confirm).setOnClickListener {
            // 로그아웃 api 연결
            logoutRetrofitCall()
            dialog.dismiss()

        }
        myLayout.findViewById<Button>(R.id.btn_cancel_logout).setOnClickListener {
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
                    val sharedPref = context?.getSharedPreferences("com.shall_we", Context.MODE_PRIVATE)
                    sharedPref?.edit()?.putString("access_token", null)?.apply()
                    sharedPref?.edit()?.putString("refresh_token", null)?.apply()

                    App.accessToken = sharedPref?.getString("access_token", null)
                    App.refreshToken = sharedPref?.getString("refresh_token", null)

                    Log.d("login", "access token ${App.accessToken}")
                    Log.d("login", "refresh token ${App.refreshToken}")

                    //로그인 화면으로 돌아가기
                    val intent = Intent(requireContext(), LoginActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                }

                RESPONSE_STATE.FAIL -> {
                    Log.d("retrofit", "api 호출 에러")
                }
            }
        })

    }

}