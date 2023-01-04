package com.alex.sobapp.repository

import com.alex.sobapp.utils.RetrofitClient

class WendaListRepository {
    suspend fun getWendaList(page: Int, state: String, category: String) =
        RetrofitClient.apiService.getWendaList(page, state, category).apiData()
}