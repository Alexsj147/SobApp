package com.alex.sobapp.repository

import com.alex.sobapp.utils.RetrofitClient

class ArticleCommentRepository {
    suspend fun getArticleComment(articleId: String, page: Int) =
        RetrofitClient.apiService.getArticleComments(articleId, page).apiData()
}