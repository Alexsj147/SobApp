package com.alex.sobapp.apiService.domain

data class CourseList(
    val currentPage: Double,
    val hasNext: Boolean,
    val hasPre: Boolean,
    val list: List<Course>,
    val pageSize: Double,
    val total: Double,
    val totalPage: Double
){
    data class Course(
        val avatar: String,
        val buyCount: Double,
        val cover: String,
        val id: String,
        val keepUpdate: String,
        val lev: String,
        val price: Double,
        val teacherId: String,
        val teacherName: String,
        val title: String,
        val viewCount: Double
    )
}

