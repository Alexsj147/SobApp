package com.alex.sobapp.repository

import com.alex.sobapp.utils.RetrofitClient

class RecommendRepository {
    suspend fun getRecommendInfo(page: Int) =
        RetrofitClient.apiService.getRecommendInfo(page).apiData()
}