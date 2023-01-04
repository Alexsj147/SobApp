package com.alex.sobapp.repository

import com.alex.sobapp.utils.RetrofitClient

class CourseRepository {
    //课程详情
    suspend fun getCourseDetail(courseId: String) =
        RetrofitClient.apiService.getCourseDetail(courseId).apiData()

    //课程章节内容
    suspend fun getCourseChapter(courseId: String) =
        RetrofitClient.apiService.getCourseChapter(courseId).apiData()

    //创建播放凭证
    suspend fun getVideo(videoId: String) = RetrofitClient.apiService.getVideo(videoId).apiData()
}