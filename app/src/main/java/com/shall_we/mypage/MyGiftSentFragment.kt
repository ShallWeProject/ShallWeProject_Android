package com.shall_we.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.shall_we.databinding.FragmentMyGiftSentBinding
import java.time.LocalDate
import java.time.LocalTime

class MyGiftSentFragment : Fragment() {
    private lateinit var viewBinding: FragmentMyGiftSentBinding
    private lateinit var adapter: MyGiftAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentMyGiftSentBinding.inflate(layoutInflater)
        setupRecyclerView()
        return viewBinding.root
    }
    private fun setupRecyclerView() {
        adapter = MyGiftAdapter(dataList = createMyGiftDataList())
        viewBinding.recyclerView.adapter = adapter
        viewBinding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // 데이터 리스트 설정
        val dataList = createMyGiftDataList()
        // adapter.setDataList(dataList)
    }

    private fun createMyGiftDataList(): List<MyGiftDto> {
        // 데이터 리스트 생성
        return listOf(
            MyGiftDto(
                1,
                "Title 1",
                "Description 1",
                LocalDate.now(),
                LocalTime.now(),
                "Cancellation 1",
                "MessageImgUrl 1",
                "Message 1"
            ),
            MyGiftDto(
                2,
                "Title 2",
                "Description 2",
                LocalDate.now(),
                LocalTime.now(),
                "Cancellation 2",
                "MessageImgUrl 2",
                "Message 2"
            ),
        )
    }
}