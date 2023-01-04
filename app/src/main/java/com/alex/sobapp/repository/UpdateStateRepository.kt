package com.alex.sobapp.repository

import com.alex.sobapp.utils.RetrofitClient

class UpdateStateRepository {
    suspend fun updateDynamicState(msgId: String) =
        RetrofitClient.apiService.updateDynamicState(msgId)
}