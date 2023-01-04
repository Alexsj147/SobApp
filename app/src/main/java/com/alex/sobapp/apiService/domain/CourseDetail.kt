package com.alex.sobapp.apiService.domain

data class CourseDetail(
    val avatar: String,
    val buyCount: Double,
    val categoryOne: String,
    val categoryTwo: String,
    val cover: String,
    val description: String,
    val id: String,
    val isVipFree: String,
    val labels: List<String>,
    val lev: Double,
    val price: Double,
    val status: String,
    val teacherId: String,
    val teacherName: String,
    val title: String,
    val viewCount: Double
)