package com.alex.sobapp.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alex.sobapp.apiService.domain.FocusAndFansListInfo
import com.alex.sobapp.repository.FocusAndFansListRepository
import com.alex.sobapp.utils.Constants.TAG
import com.alex.sobapp.utils.LoadState
import kotlinx.coroutines.launch
import java.lang.NullPointerException

class FocusAndFansListViewModel : ViewModel() {

    private val focusAndFansListRepository by lazy {
        FocusAndFansListRepository()
    }

    companion object {
        const val DEFAULT_PAGE = 1
    }

    //当前页码
    private var currentPage = DEFAULT_PAGE
    private var isLoadMore: Boolean = false

    val focusValue = MutableLiveData<MutableList<FocusAndFansListInfo.ListItem>>()
    val fansValue = MutableLiveData<MutableList<FocusAndFansListInfo.ListItem>>()
    var loadState = MutableLiveData<LoadState>()
    var focusNum = MutableLiveData<Int>()
    var fansNum = MutableLiveData<Int>()

    private fun getFocusList(userId: String, page: Int) {
        viewModelScope.launch {
            try {
                val focusList = focusAndFansListRepository.getFocusList(userId, page)
                val oldValue = focusValue.value ?: mutableListOf()
                //关注数
                focusNum.postValue(focusList.total)
                //关注的人的信息
                val list = focusList.list
                //Log.d(TAG, "list is $list ")
                if (!isLoadMore) {
                    oldValue.clear()
                }
                oldValue.addAll(list)
                //Log.d(TAG, "oldValue is $oldValue ")
                focusValue.postValue(oldValue)
                //Log.d(TAG, "viewModel focusValue is ${focusValue.value} ")
                if (list.isEmpty()) {
                    loadState.value =
                        if (isLoadMore) LoadState.LOADING_MORE_EMPTY else LoadState.EMPTY
                } else {
                    loadState.value = LoadState.SUCCESS
                }
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

    private fun getFansList(userId: String, page: Int) {
        viewModelScope.launch {
            try {
                val fansList = focusAndFansListRepository.getFansList(userId, page)
                val oldValue = fansValue.value ?: mutableListOf()
                //粉丝数
                fansNum.postValue(fansList.total)
                //关注的人的信息
                val list = fansList.list

                if (!isLoadMore) {
                    oldValue.clear()
                }
                oldValue.addAll(list)

                fansValue.postValue(oldValue)
                //Log.d(TAG, "viewModel fansValue is ${fansValue.value} ")
                if (list.isEmpty()) {
                    loadState.value =
                        if (isLoadMore) LoadState.LOADING_MORE_EMPTY else LoadState.EMPTY
                } else {
                    loadState.value = LoadState.SUCCESS
                }
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

    fun loadFocusList(userId: String) {
        isLoadMore = false
        loadState.value = LoadState.LOADING
        this.getFocusList(userId, currentPage)
    }

    fun loadMoreFocusList(userId: String) {
        isLoadMore = true
        loadState.value = LoadState.LOADING_MORE_LOADING
        currentPage++
        this.getFocusList(userId, currentPage)
    }

    fun loadFansList(userId: String) {
        isLoadMore = false
        loadState.value = LoadState.LOADING
        this.getFansList(userId, currentPage)
    }

    fun loadMoreFansList(userId: String) {
        isLoadMore = true
        loadState.value = LoadState.LOADING_MORE_LOADING
        currentPage++
        this.getFansList(userId, currentPage)
    }
}