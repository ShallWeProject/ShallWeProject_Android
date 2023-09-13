package com.shall_we.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.shall_we.R
import com.shall_we.databinding.ToolbarBinding

abstract class BaseFragment<T : ViewDataBinding>(
    @LayoutRes private val layoutResId: Int
) : Fragment() {
    private var _binding: T? = null
    protected val binding get() = _binding!!
    // 뒤로가기 버튼을 눌렀을 때를 위한 callback 변수
    private lateinit var callback: OnBackPressedCallback
    // 툴바 변수들
    protected lateinit var toolbar: Toolbar
    //protected lateinit var toolbarmenusearch: MenuItem
   // protected lateinit var toolbarmenunotification: MenuItem

    protected open fun savedatainit(){}

    protected abstract fun init()

    protected fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }



    // 뒤로가기 버튼을 눌렀을 때
    protected fun backpress() {
        // 최상위 Fragment가 아닐 때만 뒤로가기 버튼 활성화
        // -> 최상위 Fragment는 bottomnavigationview에 올라가는 것이기 때문에 뒤로가기 버튼을 활성화 하면
        // Activity의 supportfragmentmanger의 stack에서 popbackstack 하게 된다.( 단, 메뉴 선택 기록을 기록했을 경우 )
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (parentFragmentManager.backStackEntryCount != 0) {
                    parentFragmentManager
                        .popBackStackImmediate(null, 0)
                } //else if (toolbarmenusearch.isActionViewExpanded) { // 검색창을 눌렀을 경우
                 //   toolbarmenusearch.collapseActionView()
               // } else {
               //     requireActivity().finish()
               // }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    protected fun initSubAppbar(name: String, backbtn: Boolean, item: Boolean) {
        if(name == "홈"||name=="경험상세"){
            toolbar.setLogo(R.drawable.logo_text)
        }

        else{

        }
        if (backbtn) {
            // 뒤로가기 버튼 추가
            toolbar.setNavigationIcon(R.drawable.back_btn)
        } else {
            // 뒤로가기 버튼 지우기
            toolbar.setNavigationIcon(null)
        }
     //   toolbarmenunotification.setVisible(item)
     //   toolbarmenusearch.setVisible(item)
    }

    protected fun initAppbar(
        toolbarBinding: ToolbarBinding,
        firstlayoutname: String,
        backbtn: Boolean,
        item : Boolean
    ) {
        // 툴바, 툴바 검색 기록 레이아웃, 툴바 메뉴 연결
        toolbar = toolbarBinding.toolbarToolbar
        // 툴바 메뉴 추가
       // toolbar.inflateMenu(R.menu.menu_appbar)
       // toolbarmenunotification = toolbar.menu.findItem(R.id.notification)
       // toolbarmenusearch = toolbar.menu.findItem(R.id.search)

        initSubAppbar(firstlayoutname, backbtn, item)
        // 툴바 뒤로가기 버튼 클릭 시 효과
        toolbar.setNavigationOnClickListener {
            if (parentFragmentManager.backStackEntryCount > 0) {
                parentFragmentManager.popBackStackImmediate(null, 0)
            }else{
                requireActivity().onBackPressed()
            }
        }

        backpress()

        //     toolbarmenusearch.setOnMenuItemClickListener {
        //        val intent = Intent(requireContext(), SearchActivity::class.java)
        //        startActivity(intent)
        //        true
        //     }
      //  toolbarmenunotification.setOnMenuItemClickListener {
            true
       // }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, layoutResId, container, false)
        if(savedInstanceState == null){
            savedatainit()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        init()
        // 검색시 필요한 데이터
//        backpress() -> 백 버튼을 baseFragment에 넣으면 baseFragment를 상속하는 fragment들을 호출할 때마다 back callback이 초기화 된다.
        // 그래서 backbutton 이벤트 처리시 사용되는 toolbarmenusearch 변수가 초기화 되어 있지 않아 에러를 발생하게 된다.
        // parentFragment에서 한번만 초기화되면 되므로 backpress()메소드를 parentFragment에서 한번만 호출하는 initAppbar()메소드 안에 넣는다.
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // 프래그먼트가 종료되면 callback 변수 제거
    override fun onDetach() {
        super.onDetach()
        if (::callback.isInitialized) {
            callback.remove()
        }
    }

    override fun onStart() {
        super.onStart()
        val supportActionBar = (requireActivity() as AppCompatActivity).supportActionBar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    // Activity에서 fragment들을 바꿀때마다 각각의 fragment들에서 초기화된 toolbarmenusearch도 같이 바껴야한다.
    // 하지만 hide show로 바꾸었기 때문에 Activity는 toolbarmenusearch값을 가장 마지막으로 초기화 시킨 fragment의 값으로 저장한다.(초기화 가능횟수는 하단바 fragment 갯수로 제한된다.)
    // 그래서 초기화된 fragment들은 hide show가 바뀔 때마다 toolbarmenusearch값을 갱신시키지 못한다.
    // fragment들의 hide show 상태를 구분할 수 있는 메소드에 backpress 이벤트를 다시 초기화시켜 toolbarmenusearch값을 현재 보고 있는 fragment의 toolbarmenusearch값으로 갱신한다.
}