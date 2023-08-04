package com.shall_we

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
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
    object MySharedData {
        var pageFlag : Int = 1 // 다른 파일에서 이 변수에 접근하여 값을 변경하거나 사용할 수 있습니다.
    }

    val drawerData = mutableListOf<DrawerData>()
     //페이지 번호

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
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {

            }
            override fun onDrawerOpened(drawerView: View) {

            }
            override fun onDrawerClosed(drawerView: View) {
                if (MySharedData.pageFlag == 1) {
                    binding.navBottom.menu.findItem(R.id.menu_home).setChecked(true)
                }
                else if(MySharedData.pageFlag == 2){
                    binding.navBottom.menu.findItem(R.id.menu_mypage).setChecked(true)

                }
            }

            override fun onDrawerStateChanged(newState: Int) {}
        }

        binding.mainDrawerLayout.addDrawerListener(listener)

//        drawerSelected(supportFragmentManager)

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
                    MySharedData.pageFlag = 1
                    supportActionBar?.setDisplayHomeAsUpEnabled(false)
                }
                R.id.menu_mypage -> {
                    fragmentManager.beginTransaction().replace(binding.navHostFragment.id, MypageFragment()).commit()
                    MySharedData.pageFlag = 2
                    supportActionBar?.setDisplayHomeAsUpEnabled(false)

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
data class DrawerData(
    val name: String,
    val img: Int // 이미지 리소스 ID로 변경
)

