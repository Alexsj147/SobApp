package com.alex.sobapp.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alex.sobapp.apiService.domain.MoYuInfoList
import com.alex.sobapp.repository.MoYuInfoRepository
import com.alex.sobapp.utils.LoadState
import kotlinx.coroutines.launch
import java.lang.NullPointerException

class MoYuInfoViewModel : ViewModel() {

    private val moYuInfoRepository by lazy {
        MoYuInfoRepository()
    }

    companion object {
        const val DEFAULT_PAGE = 1
    }

    //当前页码
    private var currentPage = DEFAULT_PAGE
    private var isLoadMore: Boolean = false

    val contentValue = MutableLiveData<MutableList<MoYuInfoList.MoYuItem>>()
    val loadState = MutableLiveData<LoadState>()

    private fun getMoYuList(topicId: String, page: Int) {
        viewModelScope.launch {
            try {
                Log.d("myLog", "loadState before is ${loadState.value}")
                val moYuList = moYuInfoRepository.getMoYuList(topicId, page)
                val oldValue: MutableList<MoYuInfoList.MoYuItem> =
                    contentValue.value ?: arrayListOf()
                val list = moYuList.data.list
                if (!isLoadMore) {
                    oldValue.clear()
                }
                oldValue.addAll(list)
                contentValue.postValue(oldValue)
                if (list.isEmpty()) {
                    loadState.value =
                        if (isLoadMore) LoadState.LOADING_MORE_EMPTY else LoadState.EMPTY
                } else {
                    loadState.value =
                        if (isLoadMore) LoadState.LOADING_MORE_SUCCESS else LoadState.SUCCESS
                }
                Log.d("myLog", "loadState after is ${loadState.value}")
            } catch (e: Exception) {
                currentPage--
                e.printStackTrace()
                if (e is NullPointerException) {
                    //没有更多的时候，会空指针
                    loadState.value = LoadState.EMPTY
                } else {
                    loadState.value =
                        if (isLoadMore) LoadState.LOADING_MORE_ERROR else LoadState.ERROR
                }
            }
        }
    }

    fun loadContent(topicId: String) {
        isLoadMore = false
        loadState.value = LoadState.LOADING
        //currentPage = 14//测试使用
        currentPage = DEFAULT_PAGE
        this.getMoYuList(topicId, currentPage)
    }

    fun loadMoreContent(topicId: String) {
        currentPage++
        isLoadMore = true
        loadState.value = LoadState.LOADING_MORE_LOADING
        this.getMoYuList(topicId, currentPage)
    }

}