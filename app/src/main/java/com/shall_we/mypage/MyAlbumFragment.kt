package com.shall_we.mypage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.shall_we.R
import com.shall_we.databinding.FragmentMyAlbumBinding

class MyAlbumFragment : Fragment() {
    private lateinit var viewBinding: FragmentMyAlbumBinding
    private lateinit var adapter: MyAlbumAdapter

    var albumData = mutableListOf<MyAlbumDto>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentMyAlbumBinding.inflate(inflater, container, false)
//        (activity as AppCompatActivity).findViewById<ExtendedFloatingActionButton>(R.id.fab_album).show()

        viewBinding.ivLeft.setOnClickListener {
            // 이전 추억으로 이동
        }


        viewBinding.ivRight.setOnClickListener {
            // 다음 추억으로 이동
        }
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // RecyclerView에 GridLayoutManager 적용
        val gridLayoutManager = GridLayoutManager(requireContext(), 2)
        viewBinding.recyclerAlbumView.layoutManager = gridLayoutManager

        // Adapter 설정
        adapter = MyAlbumAdapter(requireContext())
        viewBinding.recyclerAlbumView.adapter = adapter
        // adapter.datas = // 데이터 리스트 설정

        albumData.apply {
            add(
                MyAlbumDto(
                    R.drawable.add_photo
                )
            )
            add(
                MyAlbumDto(
                    R.drawable.person1
                )
            )
            add(
                MyAlbumDto(
                    R.drawable.person1
                )
            )
            add(
                MyAlbumDto(
                    R.drawable.person1
                )
            )
            add(
                MyAlbumDto(
                    R.drawable.person1
                )
            )
            add(
                MyAlbumDto(
                    R.drawable.person1
                )
            )
            add(
                MyAlbumDto(
                    R.drawable.person1
                )
            )
            add(
                MyAlbumDto(
                    R.drawable.person1
                )
            )
            add(
                MyAlbumDto(
                    R.drawable.person1
                )
            )
            add(
                MyAlbumDto(
                    R.drawable.person1
                )
            )

            adapter.datas = albumData
            adapter.notifyDataSetChanged()
        }

//        (activity as AppCompatActivity).findViewById<ExtendedFloatingActionButton>(R.id.fab_album).hide()

    }

    override fun onDestroyView() {
        super.onDestroyView()
//        (activity as AppCompatActivity).findViewById<ExtendedFloatingActionButton>(R.id.fab_album).show()
    }
    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MyAlbumFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}