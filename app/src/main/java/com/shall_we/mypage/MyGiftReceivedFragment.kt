package com.shall_we.mypage

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
    private fun setupRecyclerView(recyclerView: RecyclerView, giftData: ArrayList<MyGiftData>) {
        adapter = MyGiftAdapter(requireContext(),parentFragmentManager)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        Log.d("setupRecycler giftdata: ", "$giftData")

        adapter.datas = giftData
        adapter.notifyDataSetChanged()
    }

    private fun retrofitApiCall(){
        RetrofitManager.instance.usersGiftReceive(
            completion = { responseState, responseBody ->
                when (responseState) {
                    RESPONSE_STATE.OKAY -> {
                        Log.d("retrofit", "gift receive api : ${responseBody?.size}, ${responseBody!!}")
                        giftData.apply {
                            addAll(responseBody)
                        }
                        setupRecyclerView(viewBinding.recyclerView, giftData)

                        Log.d("giftdata: ", "$giftData")
                    }

                    RESPONSE_STATE.FAIL -> {
                        Log.d("retrofit", "api 호출 에러")
                    }
                }
            })
    }
}