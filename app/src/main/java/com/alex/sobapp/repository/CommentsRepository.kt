package com.alex.sobapp.repository

import com.alex.sobapp.utils.RetrofitClient

class CommentsRepository {
    suspend fun getComments(commentId: String, page: Int) =
        RetrofitClient.apiService.getDynamicComments(commentId, page)
}