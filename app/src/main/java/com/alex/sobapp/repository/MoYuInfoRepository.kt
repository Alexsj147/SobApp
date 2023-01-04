package com.alex.sobapp.repository

import com.alex.sobapp.utils.RetrofitClient

class MoYuInfoRepository {
    suspend fun getMoYuList(topicId: String, page: Int) =
        RetrofitClient.apiService.getMoYuList(topicId, page)
}