package com.alex.sobapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alex.sobapp.apiService.domain.ArticleContent
import com.alex.sobapp.repository.ArticleRepository
import com.alex.sobapp.utils.LoadState
import kotlinx.coroutines.launch
import java.lang.Exception

class ArticleViewModel : ViewModel() {

    private val articleRepository by lazy {
        ArticleRepository()
    }

    val contentValue = MutableLiveData<ArticleContent>()
    val loadState = MutableLiveData<LoadState>()

    private fun getArticle(articleId: String) {
        viewModelScope.launch {
            try {
                val articleContent = articleRepository.getArticle(articleId)
                contentValue.postValue(articleContent)
                loadState.postValue(LoadState.SUCCESS)
            } catch (e: Exception) {
                e.printStackTrace()
                loadState.postValue(LoadState.ERROR)
            }
        }
    }

    fun loadArticle(articleId: String) {
        loadState.value = LoadState.LOADING
        this.getArticle(articleId)
    }
}