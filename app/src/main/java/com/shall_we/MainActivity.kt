package com.shall_we

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout.DrawerListener
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.shall_we.databinding.ActivityMainBinding
import com.shall_we.home.HomeFragment


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    var pageFlag : Int = 1 //페이지 번호

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction().replace(binding.navHostFragment.id, HomeFragment()).commit() // 첫 페이지는 home
        binding.navBottom.selectedItemId = R.id.menu_home
        // navigationBottomView 등록: 하단바 fragment id(bottom_navigation) 등록
        transitionNavigationBottomView(binding.navBottom, supportFragmentManager)

        val toolbar : Toolbar = binding.tbMain
        setSupportActionBar(toolbar)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.back_btn) // 뒤로가기 버튼 이미지 설정
        supportActionBar?.setDisplayHomeAsUpEnabled(false) // 뒤로가기 버튼 true이면 나옴
        supportActionBar?.setTitle("")

        var listener: DrawerListener = object : DrawerListener { // 드로어레이이웃 닫힐 때 전에 열려 있던 탭으로 이동하도록
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {}
            override fun onDrawerOpened(drawerView: View) {}
            override fun onDrawerClosed(drawerView: View) {
                if (pageFlag == 1) {
                    supportFragmentManager.beginTransaction().replace(binding.navHostFragment.id, HomeFragment()).commit()
                    binding.navBottom.selectedItemId = R.id.menu_home
                }
                else if(pageFlag == 2){
                    supportFragmentManager.beginTransaction().replace(binding.navHostFragment.id, MypageFragment()).commit()
                    binding.navBottom.selectedItemId = R.id.menu_mypage

                }
            }
            override fun onDrawerStateChanged(newState: Int) {}
        }

        binding.mainDrawerLayout.addDrawerListener(listener)


    }

    private fun transitionNavigationBottomView(bottomView: BottomNavigationView, fragmentManager: FragmentManager){ // 네비게이션 바 프래그먼트 이동
        bottomView.setOnItemSelectedListener {
            it.isChecked = true
            when(it.itemId){
                R.id.menu_category -> {
                    binding.mainDrawerLayout.openDrawer(GravityCompat.START)
                }
                R.id.menu_home -> {
                    fragmentManager.beginTransaction().replace(binding.navHostFragment.id, HomeFragment()).commit()
                    pageFlag = 1
                    supportActionBar?.setDisplayHomeAsUpEnabled(false)
                }
                R.id.menu_mypage -> {
                    fragmentManager.beginTransaction().replace(binding.navHostFragment.id, MypageFragment()).commit()
                    pageFlag = 2
                    supportActionBar?.setDisplayHomeAsUpEnabled(true)

                }
                else -> Log.d("test", "error") == 0
            }
            Log.d("test", "final") == 0
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                //toolbar의 back키 눌렀을 때 동작
                // 액티비티 이동
//                Log.d("toolbar","툴바 뒤로가기 버튼 클릭")
//                finish()
                return true
            }

        }
        return super.onOptionsItemSelected(item)
    }


}

