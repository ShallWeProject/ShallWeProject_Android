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
        binding.viewpager.apply {
            adapter = myVPAdapter
        }

        val tabTitleArray = arrayOf(
            "보낸 선물함",
            "받은 선물함",
        )

        TabLayoutMediator(binding.tabs, binding.viewpager) { tab, position ->
            tab.text = tabTitleArray[position]
        }.attach()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding.fabAlbum.setOnClickListener {
            val tabs = requireActivity().findViewById<View>(R.id.tabs)
            tabs.visibility = View.GONE
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
            }
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackPressedCallback)
    }
}