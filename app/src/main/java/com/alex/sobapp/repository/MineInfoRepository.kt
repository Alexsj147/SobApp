package com.alex.sobapp.repository

import com.alex.sobapp.utils.RetrofitClient

class MineInfoRepository {
    //获取文章评论列表
    suspend fun getMineArticleComment(page: Int) =
        RetrofitClient.apiService.getMineArticleComment(page).apiData()

    //获取动态评论
    suspend fun getMineDynamicComment(page: Int) =
        RetrofitClient.apiService.getDynamicComment(page).apiData()

    //获取问答列表
    suspend fun getMineWendaList(page: Int) = RetrofitClient.apiService.getMineWendaList(page).apiData()

    //获取回复我的
    suspend fun getMineAtMeInfo(page: Int) = RetrofitClient.apiService.getAtMeInfo(page).apiData()

    //获取点赞列表
    suspend fun getMineThumbUpList(page: Int) =
        RetrofitClient.apiService.getThumbUpList(page).apiData()

    //获取系统消息
    suspend fun getSystemInfo(page: Int) = RetrofitClient.apiService.getSystemInfo(page).apiData()

}