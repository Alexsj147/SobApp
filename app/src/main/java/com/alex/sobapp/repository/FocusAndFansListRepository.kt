package com.alex.sobapp.repository

import com.alex.sobapp.utils.RetrofitClient

class FocusAndFansListRepository {
    suspend fun getFocusList(userId: String, page: Int) =
        RetrofitClient.apiService.getFocusList(userId, page).apiData()

    suspend fun getFansList(userId: String, page: Int) =
        RetrofitClient.apiService.getFansList(userId, page).apiData()
}