package com.alex.sobapp.repository

import com.alex.sobapp.utils.RetrofitClient

class WendaDetailRepository {
    //获取问答详情
    suspend fun getWendaDetail(wendaId: String) =
        RetrofitClient.apiService.getWendaDetail(wendaId).apiData()

    //获取问答答案列表
    suspend fun getWendaAnswerList(wendaId: String, page: Int) =
        RetrofitClient.apiService.getWendaAnswerList(wendaId, page)

    //获取相关问题
    suspend fun getRelatedQuestion(wendaId: String, size: Int) =
        RetrofitClient.apiService.getRelatedQuestion(wendaId, size).apiData()
}