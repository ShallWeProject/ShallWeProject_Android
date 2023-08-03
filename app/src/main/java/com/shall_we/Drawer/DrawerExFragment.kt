package com.shall_we.Drawer

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shall_we.R
import com.shall_we.databinding.FragmentDrawerExBinding
import com.shall_we.home.HomeRealtimeFragment
import com.shall_we.home.HomeRealtimeFragment.GridSpaceItemDecoration
import com.shall_we.home.HomeRecomFragment
import com.shall_we.home.RecomAdapter
import com.shall_we.home.RecomData
import com.shall_we.home.dpToPx

class DrawerExFragment : Fragment() {

    lateinit var drawerAdapter: DrawerAdapter
    val drawerData = mutableListOf<DrawerData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentDrawerExBinding.inflate(inflater,container,false)
        initRecycler(binding.rvEx)
        return binding.root
    }

    fun initRecycler(rv: RecyclerView) {

        drawerAdapter = DrawerAdapter(requireContext())
        val layoutManager = GridLayoutManager(context, 4, GridLayoutManager.VERTICAL, false)
        rv.layoutManager = layoutManager
        rv.adapter = drawerAdapter


        drawerData.apply {
            add(DrawerData(img = R.drawable.craft, name = "공예"))
            add(DrawerData(img = R.drawable.baking, name = "베이킹"))
            add(DrawerData(img = R.drawable.art, name = "문화예술"))
            add(DrawerData(img = R.drawable.outdoor, name = "아웃도어"))
            add(DrawerData(img = R.drawable.sport, name = "스포츠"))


            drawerAdapter.datas = drawerData
            drawerAdapter.notifyDataSetChanged()



        }
        val spaceDecoration = SpaceItemDecoration(dpToPx(15), dpToPx(13))

        rv.addItemDecoration(spaceDecoration)


    }

    class SpaceItemDecoration(private val verticalSpaceWidth:Int, private val horizontalSpaceWidth:Int):RecyclerView.ItemDecoration(){
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            outRect.right = horizontalSpaceWidth
            outRect.top = verticalSpaceWidth
        }
    }
}
