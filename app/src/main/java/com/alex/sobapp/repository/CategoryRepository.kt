package com.alex.sobapp.repository

import com.alex.sobapp.utils.RetrofitClient

class CategoryRepository {
    suspend fun getCategoryList() = RetrofitClient.apiService.getCategoryList()
}