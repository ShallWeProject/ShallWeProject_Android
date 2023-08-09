package com.shall_we.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shall_we.databinding.FragmentFaqBinding

class FaqFragment : Fragment() {
    private lateinit var viewBinding: FragmentFaqBinding
    private lateinit var adapter: FaqAdapter

    val faqData = mutableListOf<FaqDto>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentFaqBinding.inflate(layoutInflater)
        setupRecyclerView(viewBinding.recyclerView)
        return viewBinding.root
    }
    private fun setupRecyclerView(recyclerView: RecyclerView) {
        adapter = FaqAdapter(requireContext())
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        faqData.apply {
            add(
                FaqDto(
                    "셸위는 어떠한 서비스인가요?",
                    "셸위는 추억을 남길 수 있도록 돕는 경험 선물 서비스입니다. 특별한 날, 소중한 사람에게 잊지못할 추억을 선물하세요 :)"
                    )
            )
            add(
                FaqDto(
                    "예약 가능한 시간이 조금밖에 안보여요.",
                    "실시간 예약 현황에 따라 예약이 마감된 일시는 자동적으로 표시되지 않습니다. 예약 가능한 다른 일시를 선택해주세요."
                )
            )
            add(
                FaqDto(
                    "예약을 변경하고 싶어요.",
                    "예약 변경은 이용일로부터 3일 전까지만 가능합니다. 마이페이지 > 보낸 선물함에서 예약변경 을 클릭하여 원하시는 일시로 예약을 변경해주세요."
                )
            )
            add(
                FaqDto(
                    "예약을 취소하고 싶어요.",
                    "예약 취소는 이용일로부터 7일 전까지만 가능합니다. 이후 예약을 취소하실 경우, 작가님께 개인적으로 연락해주세요. 취소 일시에 따라 별도의 수수료가 부과될 수 있다는 점 참고 부탁드립니다."
                )
            )
            add(
                FaqDto(
                    "예약 취소 요청을 철회하고 싶어요.",
                     "예약 취소 요청 철회는 시스템 상 불가능합니다. 예약 절차를 다시 진행해주세요."                )
            )
            add(
                FaqDto(
                    "경험 결제는 어떻게 할 수 있나요?",
                    "예약을 완료하시면 작가님께 예약자님의 예약 정보가 전달됩니다. 작가님들께서 안내해주시는 절차에 따라 결제를 진행해주세요. 결제를 완료하셔야 예약이 확정됩니다."
                )
            )
            add(
                FaqDto(
                    "추억 사진첩은 무엇인가요?",
                    "추억 사진첩은 소중한 사람들과 함께한 추억을 업로드할 수 있는 사진첩입니다. 경험별로 추억을 업로드해보세요."
                )
            )
            add(
                FaqDto(
                    "이외에도 궁금한 점이 있어요.",
                    "기타 문의사항은 info@shallwee.com으로 전달 부탁드립니다."
                )
            )
            adapter.datas = faqData
            adapter.notifyDataSetChanged()
        }
    }
}