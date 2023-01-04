package com.alex.sobapp.repository

import com.alex.sobapp.utils.RetrofitClient

class UserInfoRepository {
    suspend fun getUserInfo(userId: String) =
        RetrofitClient.apiService.getUserInfo(userId).apiData()
}