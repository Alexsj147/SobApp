package com.alex.sobapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alex.sobapp.apiService.domain.DynamicComments
import com.alex.sobapp.repository.CommentsRepository
import com.alex.sobapp.utils.LoadState
import kotlinx.coroutines.launch
import java.lang.Exception
import java.lang.NullPointerException

class CommentsViewModel : ViewModel() {

    private val commentsRepository by lazy {
        CommentsRepository()
    }

    companion object {
        const val DEFAULT_PAGE = 1
    }

    //当前页码
    private var currentPage = DEFAULT_PAGE
    private var isLoadMore: Boolean = false

    val contentValue = MutableLiveData<MutableList<DynamicComments.Comments>>()
    val loadState = MutableLiveData<LoadState>()

    private fun getDynamicComment(commentId: String, page: Int) {
        viewModelScope.launch {
            try {
                val comments = commentsRepository.getComments(commentId, page)
                val list = comments.data.list
                val oldValue: MutableList<DynamicComments.Comments> =
                    contentValue.value ?: arrayListOf()
                if (!isLoadMore) {
                    oldValue.clear()
                }
                oldValue.addAll(list)
                contentValue.postValue(oldValue)
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

    fun loadComments(commentId: String) {
        isLoadMore = false
        loadState.value = LoadState.LOADING
        this.getDynamicComment(commentId, currentPage)
    }

    fun loadMoreComments(commentId: String) {
        isLoadMore = true
        currentPage++
        loadState.value = LoadState.LOADING_MORE_LOADING
        this.getDynamicComment(commentId, currentPage)
    }
}