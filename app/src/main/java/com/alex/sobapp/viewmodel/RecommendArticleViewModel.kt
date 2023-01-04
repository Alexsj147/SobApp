package com.alex.sobapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alex.sobapp.apiService.domain.RecommendArticle
import com.alex.sobapp.repository.RecommendArticleRepository
import com.alex.sobapp.utils.LoadState
import kotlinx.coroutines.launch
import java.lang.Exception
import java.lang.NullPointerException

class RecommendArticleViewModel : ViewModel() {

    private val recommendArticleRepository by lazy {
        RecommendArticleRepository()
    }

    companion object {
        const val DEFAULT_SIZE = 10
    }

    //当前页码
    private var currentSize = DEFAULT_SIZE
    private var isLoadMore: Boolean = false

    val contentValue = MutableLiveData<MutableList<RecommendArticle.Data>>()
    val loadState = MutableLiveData<LoadState>()


    private fun getRecommendArticle(articleId: String, size: Int) {
        viewModelScope.launch {
            try {
                val recommendArticle =
                    recommendArticleRepository.getRecommendArticle(articleId, size)
                val list = recommendArticle.data
                val oldValue: MutableList<RecommendArticle.Data> =
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
        this.getRecommendArticle(articleId, currentSize)
    }

    fun loadMoreArticleComments(articleId: String) {
        isLoadMore = true

        loadState.value = LoadState.LOADING_MORE_LOADING
        this.getRecommendArticle(articleId, currentSize)
    }
}