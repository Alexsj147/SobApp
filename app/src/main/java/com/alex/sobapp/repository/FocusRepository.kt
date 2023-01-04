package com.alex.sobapp.repository

import com.alex.sobapp.utils.RetrofitClient

class FocusRepository {
    //添加关注
    suspend fun addFocus(userId: String) = RetrofitClient.apiService.addFocus(userId)

    //取消关注
    suspend fun deleteFocus(userId: String) = RetrofitClient.apiService.deleteFocus(userId)
}