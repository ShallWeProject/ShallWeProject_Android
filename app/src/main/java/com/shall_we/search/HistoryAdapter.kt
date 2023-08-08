package com.shall_we.search

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.shall_we.R

class RecentAdapter (private val context: Context, searchHistoryRecyclerInterface : ISearchHistoryRecycler) : RecyclerView.Adapter<RecentAdapter.ViewHolder>() {
    var datas = mutableListOf<SearchData>()

    private var iSearchHistoryRecycler : ISearchHistoryRecycler ? = null

    init {
        Log.d("history","adapter init 호출")
        this.iSearchHistoryRecycler = searchHistoryRecyclerInterface // 인터페이스 가져옴
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_search_history,parent,false)
        return ViewHolder(view,this.iSearchHistoryRecycler!!)
    }

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datas[position])

    }

    inner class ViewHolder(view: View, searchHistoryRecyclerInterface: ISearchHistoryRecycler) : RecyclerView.ViewHolder(view),View.OnClickListener {

        private val history_item: TextView = itemView.findViewById(R.id.search_word)
        private val deleteBtn : ImageView = itemView.findViewById(R.id.delete_btn)
        private val searchItem : ConstraintLayout = itemView.findViewById(R.id.search_item)

        private var mySearchHistoryInterface : ISearchHistoryRecycler

        init {
            // 리스너 연결
            deleteBtn.setOnClickListener(this)
            searchItem.setOnClickListener(this)
            this.mySearchHistoryInterface = searchHistoryRecyclerInterface

        }
        fun bind(item: SearchData) {
            history_item.text = item.search_word
        }

        override fun onClick(p0: View?) {
            when(p0){
                deleteBtn -> {
                    Log.d("history","history 삭제")
                    this.mySearchHistoryInterface.onSearchItemDeleteClicked(adapterPosition)

                }
                searchItem -> {
                    Log.d("history","검색 아이템 클릭")
                    this.mySearchHistoryInterface.onSearchItemClicked(adapterPosition)
                }
            }
        }
    }

    fun submitList(SearchList: ArrayList<SearchData>){
        this.datas = SearchList
    }

}