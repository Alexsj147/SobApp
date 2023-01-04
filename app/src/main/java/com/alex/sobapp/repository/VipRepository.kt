package com.alex.sobapp.repository

import com.alex.sobapp.utils.RetrofitClient

class VipRepository {
    //判断是否有领取津贴
    suspend fun checkVipAllowance() = RetrofitClient.apiService.checkVipAllowance()

    //VIP领取津贴（每月）
    suspend fun getVipAllowance() = RetrofitClient.apiService.getVipAllowance()
}