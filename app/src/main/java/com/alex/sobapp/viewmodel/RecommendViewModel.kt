package com.alex.sobapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alex.sobapp.apiService.domain.RecommendList
import com.alex.sobapp.repository.RecommendRepository
import com.alex.sobapp.utils.LoadState
import kotlinx.coroutines.launch
import java.lang.Exception
import java.lang.NullPointerException

class RecommendViewModel : ViewModel() {

    private val recommendRepository by lazy {
        RecommendRepository()
    }

    companion object {
        const val DEFAULT_PAGE = 1
    }

    //当前页码
    private var currentPage = DEFAULT_PAGE
    private var isLoadMore: Boolean = false

    val contentValue = MutableLiveData<MutableList<RecommendList.Info>>()
    val loadState = MutableLiveData<LoadState>()

    private fun getRecommendList(page: Int) {

        viewModelScope.launch {
            try {
                val recommendInfo = recommendRepository.getRecommendInfo(page)
                val infoList = recommendInfo.list
                //println(infoList)
                val oldValue: MutableList<RecommendList.Info> =
                    contentValue.value ?: arrayListOf()
                oldValue.addAll(infoList)
                contentValue.postValue(oldValue)
                if (infoList.isEmpty()) {
                    loadState.value =
                        if (isLoadMore) LoadState.LOADING_MORE_EMPTY else LoadState.EMPTY
                } else {
                    loadState.value = LoadState.SUCCESS
                }
                println(loadState.value)
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

    fun loadContent() {
        isLoadMore = false
        loadState.value = LoadState.LOADING
        this.getRecommendList(currentPage)
    }

    fun loadMoreContent() {
        currentPage++
        isLoadMore = true
        loadState.value = LoadState.LOADING_MORE_LOADING
        this.getRecommendList(currentPage)
    }
}