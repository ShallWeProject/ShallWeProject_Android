package com.shall_we

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.shall_we.databinding.FragmentMypageBinding
import com.shall_we.mypage.MyAlbumFragment
import com.shall_we.mypage.MypageVPAdapter


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class MypageFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private val binding: FragmentMypageBinding by lazy {
        FragmentMypageBinding.inflate(layoutInflater)
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }


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

        binding.fab.setOnClickListener{

            val myAlbumFragment = MyAlbumFragment() // 전환할 프래그먼트 인스턴스 생성
            val fragmentTransaction = parentFragmentManager.beginTransaction()
            fragmentTransaction.add(R.id.mypage_layout, myAlbumFragment, "get")
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commitAllowingStateLoss()
            Log.d("clicked","change")

        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

//        binding.fab.setOnClickListener {
//            val albumFragment = MyAlbumFragment() // 전환할 프래그먼트 인스턴스 생성
//            val fragmentTransaction = parentFragmentManager.beginTransaction()
//            fragmentTransaction.add(R.id.mypageFragment, albumFragment, "album")
//            fragmentTransaction.addToBackStack(null)
//            fragmentTransaction.commitAllowingStateLoss()
//            Log.d("clicked","change")
//        }

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MypageFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}