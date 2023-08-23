package com.shall_we.mypage

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.shall_we.R
import com.shall_we.databinding.FragmentMypageBinding
import com.shall_we.myAlbum.MyAlbumFragment

public var isTabLayoutVisible = true
class MypageFragment : Fragment() {

    private val binding: FragmentMypageBinding by lazy {
        FragmentMypageBinding.inflate(layoutInflater)
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val myVPAdapter = super.getActivity()?.let { MypageVPAdapter(fragmentActivity = it) }
        binding.viewpager?.apply {
                adapter = myVPAdapter
            }



        val tabTitleArray = arrayOf(
            "보낸 선물함",
            "받은 선물함",
        )

        TabLayoutMediator(binding.tabs, binding.viewpager) { tab, position ->
            tab.text = tabTitleArray[position]
        }.attach()

        val position = arguments?.getInt("position",0)
        if(position != null){
            setSelectedTab(binding.tabs, position!!-1)

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        binding.fabAlbum.show()

        binding.tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                // 선택된 탭의 인덱스를 얻습니다.
                val selectedTabIndex = tab?.position ?: 2
                // 이제 선택된 탭에 대한 작업을 수행할 수 있습니다.
                // 예: 선택된 탭의 인덱스를 이용해 데이터를 업데이트하거나 다른 동작 수행.
                if (selectedTabIndex == 0) {
                    binding.fabAlbum.visibility = View.VISIBLE
                }
                else {
                    binding.fabAlbum.visibility = View.GONE
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })

        binding.fabAlbum.setOnClickListener {

//            findNavController().navigate(
//                R.id.myalbum
//            )
//            val myAlbumFragment = MyAlbumFragment() // 전환할 프래그먼트 인스턴스 생성
//            val fragmentTransaction = parentFragmentManager.beginTransaction()
//            fragmentTransaction.add(R.id.mypage_layout, myAlbumFragment, "get")
//            fragmentTransaction.addToBackStack(null)
//            fragmentTransaction.commitAllowingStateLoss()
//            Log.d("clicked","change")

            val tabs = requireActivity().findViewById<View>(R.id.tabs)
            tabs.visibility = View.GONE
//            mypageBinding.tabs.visibility = View.GONE
            // 상태 토글
//            isTabLayoutVisible = !isTabLayoutVisible

            binding.fabAlbum.visibility = View.GONE
            val myAlbumFragment = MyAlbumFragment()
            val fragmentTransaction = parentFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.mypage_layout, myAlbumFragment, "myAlbumFragment")
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commitAllowingStateLoss()
            Log.d("clicked","change")
        }

        return binding.root
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            // 뒤로가기 시 실행할 코드
            if (!isTabLayoutVisible) {
                // TabLayout 보이기
                binding.tabs.visibility = View.VISIBLE
                isTabLayoutVisible = true
            } else {
                requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, this)
                // 기본 동작 수행 (프래그먼트 종료 또는 이전 상태로 돌아가기)
            }
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        binding.fabAlbum.setOnClickListener {
//            findNavController().navigate(
//               R.id.action_mypageFragment_to_myAlbumFragment
//          )
//        }
//        binding.fabAlbum.show()
        // Fragment에서 뒤로가기 버튼 동작 처리
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackPressedCallback)
    }

    override fun onDestroyView() {
        super.onDestroyView()
//        binding.fabAlbum.hide()
    }

    private fun setSelectedTab(tabLayout: TabLayout, selectedTabIndex: Int) {
        val tabCount = tabLayout.tabCount
        if (selectedTabIndex >= 0 && selectedTabIndex < tabCount) {
            val tab = tabLayout.getTabAt(selectedTabIndex)
            tab?.select()
        }
    }

}