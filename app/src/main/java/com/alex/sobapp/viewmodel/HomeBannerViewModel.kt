package com.alex.sobapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alex.sobapp.apiService.domain.HomeBannerList
import com.alex.sobapp.repository.HomeBannerRepository
import com.alex.sobapp.utils.LoadState
import kotlinx.coroutines.launch
import java.lang.Exception
import java.lang.NullPointerException

class HomeBannerViewModel : ViewModel() {

    private val homeBannerRepository by lazy {
        HomeBannerRepository()
    }

    val contentList = MutableLiveData<MutableList<HomeBannerList.Data>>()
    val loadState = MutableLiveData<LoadState>()

    private fun getBannerList() {
        viewModelScope.launch {
            try {
                val homeBanner = homeBannerRepository.getHomeBanner()
                val data = homeBanner.data
                val bannerList = contentList.value ?: mutableListOf()
                bannerList.addAll(data)
                contentList.postValue(bannerList)
                if (bannerList.isEmpty()) {
                    loadState.value = LoadState.EMPTY
                } else {
                    loadState.value = LoadState.SUCCESS
                }
            } catch (e: Exception) {
                if (e is NullPointerException) {
                    loadState.postValue(LoadState.ERROR)
                } else {
                    loadState.postValue(LoadState.ERROR)
                }
            }
        }
    }

    fun loadBanner() {
        this.getBannerList()
    }
}