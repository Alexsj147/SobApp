package com.alex.sobapp.repository

import com.alex.sobapp.apiService.domain.DynamicBody
import com.alex.sobapp.utils.RetrofitClient

class ReleaseDynamicRepository {
    suspend fun releaseDynamic(dynamicBody: DynamicBody) =
        RetrofitClient.apiService.releaseDynamic(dynamicBody)
}