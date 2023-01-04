package com.alex.sobapp.repository

import com.alex.sobapp.utils.RetrofitClient

class UnreadInfoRepository {
    //获取未读消息
    suspend fun getUnreadInfo() = RetrofitClient.apiService.getUnreadInfo().apiData()

    //一键已读所有消息
    suspend fun oneStepRead() = RetrofitClient.apiService.oneStepRead()
}