package com.alex.sobapp.repository

import com.alex.sobapp.utils.RetrofitClient

class RecommendArticleRepository {
    suspend fun getRecommendArticle(articleId: String, size: Int) =
        RetrofitClient.apiService.getRecommendArticle(articleId, size)
}