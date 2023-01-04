package com.alex.sobapp.repository

import com.alex.sobapp.utils.RetrofitClient

class TopicRepository {
    suspend fun getTopicListInfo() = RetrofitClient.apiService.getTopicList()
}