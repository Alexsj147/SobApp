package com.alex.sobapp.repository

import com.alex.sobapp.utils.RetrofitClient

class HomeBannerRepository {
    suspend fun getHomeBanner() = RetrofitClient.apiService.getHomeBanner()
}