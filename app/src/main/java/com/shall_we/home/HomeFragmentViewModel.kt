package com.shall_we.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeFragmentViewModel : ViewModel() {
    private val _bannerItemList: MutableLiveData<List<BannerItem>> = MutableLiveData()

    val bannerItemList: LiveData<List<BannerItem>>
        get() = _bannerItemList

    fun setBannerItems(list: List<BannerItem>){
        _bannerItemList.value = list
    }

}