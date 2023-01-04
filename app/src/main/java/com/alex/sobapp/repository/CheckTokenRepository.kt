package com.alex.sobapp.repository

import com.alex.sobapp.utils.RetrofitClient

class CheckTokenRepository {
    suspend fun checkToken() = RetrofitClient.apiService.analysisToken()
}