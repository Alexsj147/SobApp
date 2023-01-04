package com.alex.sobapp.repository

import com.alex.sobapp.utils.RetrofitClient

class ArticleRepository {
    suspend fun getArticle(articleId: String) =
        RetrofitClient.apiService.getArticle(articleId).apiData()
}