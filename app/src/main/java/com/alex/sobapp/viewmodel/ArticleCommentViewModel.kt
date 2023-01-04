package com.alex.sobapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alex.sobapp.apiService.domain.ArticleComments
import com.alex.sobapp.repository.ArticleCommentRepository
import com.alex.sobapp.utils.LoadState
import kotlinx.coroutines.launch
import java.lang.Exception
import java.lang.NullPointerException

class ArticleCommentViewModel : ViewModel() {

    private val articleCommentRepository by lazy {
        ArticleCommentRepository()
    }

    companion object {
        const val DEFAULT_PAGE = 1
    }

    //当前页码
    private var currentPage = DEFAULT_PAGE
    private var isLoadMore: Boolean = false

    val contentValue = MutableLiveData<MutableList<ArticleComments.Content>>()
    val loadState = MutableLiveData<LoadState>()


    private fun getArticleComments(articleId: String, page: Int) {
        viewModelScope.launch {
            try {
                val articleComment = articleCommentRepository.getArticleComment(articleId, page)
                val list = articleComment.content
                val oldValue: MutableList<ArticleComments.Content> =
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

    fun loadArticleComments(articleId: String) {
        isLoadMore = false
        loadState.value = LoadState.LOADING
        this.getArticleComments(articleId, currentPage)
    }

    fun loadMoreArticleComments(articleId: String) {
        isLoadMore = true
        currentPage++
        loadState.value = LoadState.LOADING_MORE_LOADING
        this.getArticleComments(articleId, currentPage)
    }
}