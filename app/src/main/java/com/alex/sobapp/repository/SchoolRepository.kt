package com.alex.sobapp.repository

import com.alex.sobapp.utils.RetrofitClient

class SchoolRepository {
    //获取课程列表
    suspend fun getCourseList(page: Int) = RetrofitClient.apiService.getCourseList(page).apiData()
}