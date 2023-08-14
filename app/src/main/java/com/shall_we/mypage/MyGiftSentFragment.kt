package com.shall_we.mypage

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shall_we.R
import com.shall_we.databinding.FragmentMyGiftSentBinding
import com.shall_we.databinding.FragmentMypageBinding
import com.shall_we.myAlbum.MyAlbumFragment

class MyGiftSentFragment : Fragment() {
    private lateinit var viewBinding: FragmentMyGiftSentBinding
    private lateinit var adapter: MyGiftAdapter

    val giftData = mutableListOf<MyGiftDto>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentMyGiftSentBinding.inflate(layoutInflater)
        setupRecyclerView(viewBinding.recyclerView)

        return viewBinding.root
    }
    private fun setupRecyclerView(recyclerView: RecyclerView) {
        adapter = MyGiftAdapter(requireContext(),parentFragmentManager)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        giftData.apply {
            add(
                MyGiftDto(
                    1,
                    "[홍대] 인기 공예 클래스",
                    "테마가 있는 프라이빗 칵테일 클래스",
                    "2023.08.13",
                    "20시",
                    false,
                    R.drawable.message_img,
                    "취뽀 축하한다~~~ 주당인 널 위해 칵테일 클래스를 찾아와봤어~ 내 센스 한 번만 칭찬해주고ㅋㅎ 무쪼록 우리 광란의 밤을 보내보자고~!~!! 스냅사진도 신청했으니까 드레스코드도 블랙으로 꼭 맞춰와라잉! 다시 한 번 축하해!!"

                )
            )
            add(
                MyGiftDto(
                    2,
                    "[성수] 인기 베이킹 클래스",
                    "기념일 레터링 케이크 사지 말고 함께 만들어요",
                    "2023.04.23",
                    "16시",
                    false,
                    R.drawable.message_img2,
                    "100일 넌무너무 축하해 !! 100일 기념으로 케이크 같이 만드는거 어때? 직접 케이크 만들고 성수 맛집 가장 !!!"
                ),
            )

            adapter.datas = giftData
            adapter.notifyDataSetChanged()
        }
    }
}