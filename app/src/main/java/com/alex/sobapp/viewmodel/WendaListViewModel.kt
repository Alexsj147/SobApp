package com.alex.sobapp.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alex.sobapp.apiService.domain.WendaList
import com.alex.sobapp.repository.WendaListRepository
import com.alex.sobapp.utils.Constants
import com.alex.sobapp.utils.LoadState
import kotlinx.coroutines.launch
import java.lang.Exception
import java.lang.NullPointerException

class WendaListViewModel : ViewModel() {

    private val wendaListRepository by lazy {
        WendaListRepository()
    }

    //当前页码
    private var currentPage = Constants.DEFAULT_PAGE
    private var isLoadMore: Boolean = false

    val wendaListValue = MutableLiveData<MutableList<WendaList.Wenda>>()
    val loadState = MutableLiveData<LoadState>()

    private fun getWendaList(page: Int, state: String, category: String) {
        viewModelScope.launch {
            try {
                val wendaList = wendaListRepository.getWendaList(page, state, category)
                val list = wendaList.list
                //Log.d("myLog", "wenda list is $list")
                val oldValue = wendaListValue.value ?: arrayListOf()
                if (!isLoadMore) {
                    oldValue.clear()
                }
                oldValue.addAll(list)
                wendaListValue.postValue(oldValue)
                if (list.isEmpty()) {
                    loadState.value =
                        if (isLoadMore) LoadState.LOADING_MORE_EMPTY else LoadState.EMPTY
                } else {
                    loadState.value =
                        if (isLoadMore) LoadState.LOADING_MORE_SUCCESS else LoadState.SUCCESS
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

    fun loadWendaList(state: String, category: String) {
        loadState.postValue(LoadState.LOADING)
        isLoadMore = false
        currentPage = Constants.DEFAULT_PAGE
        this.getWendaList(currentPage, state, category)
    }

    fun loadMoreWendaList(state: String, category: String) {
        loadState.postValue(LoadState.LOADING_MORE_LOADING)
        isLoadMore = true
        currentPage++
        this.getWendaList(currentPage, state, category)
    }
}