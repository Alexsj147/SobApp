package com.alex.sobapp.repository

import com.alex.sobapp.utils.RetrofitClient

class ClassifiedRepository {
    suspend fun getClassifiedContent(categoryId: String, page: Int) =
        RetrofitClient.apiService.getClassifiedContent(categoryId, page).apiData()
}