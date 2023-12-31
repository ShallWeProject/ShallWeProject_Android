package com.shall_we.mypage

import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shall_we.databinding.FragmentMyGiftReceivedBinding
import com.shall_we.retrofit.RESPONSE_STATE
import com.shall_we.retrofit.RetrofitManager

class MyGiftReceivedFragment : Fragment() {
    private lateinit var viewBinding: FragmentMyGiftReceivedBinding
    private lateinit var adapter: MyGiftAdapter

    val giftData = ArrayList<MyGiftData>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentMyGiftReceivedBinding.inflate(layoutInflater)
        retrofitApiCall()
        return viewBinding.root
    }
    private fun setupRecyclerView(recyclerView: RecyclerView, giftData: ArrayList<MyGiftData>,expId:ArrayList<Int>) {
        adapter = MyGiftAdapter(requireContext(),parentFragmentManager)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val bottomSpaceItemDecoration = BottomSpaceItemDecoration(dpToPx(70))
        recyclerView.addItemDecoration(bottomSpaceItemDecoration)
        Log.d("setupRecycler giftdata: ", "$giftData")

        adapter.datas = giftData
        adapter.expId = expId
        adapter.notifyDataSetChanged()
    }

    private fun dpToPx(dp: Int): Int {
        val density = resources.displayMetrics.density
        return (dp.toFloat() * density).toInt()
    }
    class BottomSpaceItemDecoration(private val bottomSpaceHeight: Int) : RecyclerView.ItemDecoration() {

        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            val itemPosition = parent.getChildAdapterPosition(view)
            val itemCount = parent.adapter?.itemCount ?: 0

            if (itemPosition == itemCount - 1) {
                // 맨 마지막 아이템인 경우만 여백 추가
                outRect.bottom = bottomSpaceHeight
            }
        }
    }

    private fun retrofitApiCall(){
        RetrofitManager.instance.usersGiftReceive(
            completion = { responseState, responseBody,expId ->
                when (responseState) {
                    RESPONSE_STATE.OKAY -> {
                        Log.d("retrofit", "gift receive api : ${responseBody?.size}, ${responseBody!!}")
                        giftData.apply {
                            addAll(responseBody)
                        }
                        if (expId != null) {
                            setupRecyclerView(viewBinding.recyclerView, giftData,expId)
                        }

                        Log.d("giftdata: ", "$giftData")
                    }

                    RESPONSE_STATE.FAIL -> {
                        Log.d("retrofit", "api 호출 에러")
                    }
                }
            })
    }
}