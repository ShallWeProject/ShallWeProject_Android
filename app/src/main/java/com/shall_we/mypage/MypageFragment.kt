package com.shall_we

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.tabs.TabLayoutMediator
import com.shall_we.databinding.FragmentMypageBinding
import com.shall_we.mypage.MyAlbumFragment
import com.shall_we.mypage.MypageVPAdapter


class MypageFragment : Fragment() {
    var isTabLayoutVisible = true

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

            if (isTabLayoutVisible) {
                // TabLayout 숨기기
                binding.tabs.visibility = View.GONE
            } else {
                // TabLayout 보이기
                binding.tabs.visibility = View.VISIBLE
            }
            // 상태 토글
            isTabLayoutVisible = !isTabLayoutVisible

            binding.fabAlbum.visibility = View.GONE
            val myAlbumFragment = MyAlbumFragment()
            val fragmentTransaction = parentFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.mypage_layout, myAlbumFragment, "myAlbumFragment")
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commitAllowingStateLoss()
            Log.d("clicked","change")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        binding.fabAlbum.show()
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

}