package com.alex.sobapp.repository

import com.alex.sobapp.utils.RetrofitClient

class ThumbUpRepository {
    suspend fun getThumbUpResult(momentId: String) = RetrofitClient.apiService.thumbUp(momentId)
}