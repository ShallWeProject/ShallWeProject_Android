package com.shall_we.myAlbum

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.shall_we.R
import com.shall_we.databinding.FragmentMyAlbumBinding
import com.shall_we.mypage.MyGiftAdapter
import com.shall_we.mypage.MyGiftData

class MyAlbumFragment : Fragment() {
    private lateinit var viewBinding: FragmentMyAlbumBinding
    private lateinit var albumadapter: MyAlbumAdapter
    private lateinit var giftAdapter: MyGiftAdapter

    val albumData = mutableListOf<MyAlbumPhotoData>()
    val giftData = mutableListOf<MyGiftData>()

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
            val newFragment = newInstance("", "")
            val fragmentTransaction = parentFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.myalbum, newFragment, "myAlbumFragment")
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commitAllowingStateLoss()
        }


        viewBinding.ivRight.setOnClickListener {
            val newFragment = newInstance("", "")
            val fragmentTransaction = parentFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.myalbum, newFragment, "myAlbumFragment")
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commitAllowingStateLoss()
        }

        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // RecyclerView에 GridLayoutManager 적용
        val gridLayoutManager = GridLayoutManager(requireContext(), 2)
        viewBinding.recyclerAlbumView.layoutManager = gridLayoutManager

        // Adapter 설정
        albumadapter = MyAlbumAdapter(requireContext())
        viewBinding.recyclerAlbumView.adapter = albumadapter
        // adapter.datas = // 데이터 리스트 설정

        albumData.apply {
            add(
                MyAlbumPhotoData(
                    R.drawable.add_photo
                )
            )
            add(
                MyAlbumPhotoData(
                    R.drawable.person1
                )
            )
            add(
                MyAlbumPhotoData(
                    R.drawable.photo_big
                )
            )
            add(
                MyAlbumPhotoData(
                    R.drawable.person1
                )
            )
            add(
                MyAlbumPhotoData(
                    R.drawable.person1
                )
            )
            add(
                MyAlbumPhotoData(
                    R.drawable.person1
                )
            )
            add(
                MyAlbumPhotoData(
                    R.drawable.person1
                )
            )
            add(
                MyAlbumPhotoData(
                    R.drawable.person1
                )
            )


            albumadapter.datas = albumData
            albumadapter.notifyDataSetChanged()

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