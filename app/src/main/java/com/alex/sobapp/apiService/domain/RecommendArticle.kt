package com.alex.sobapp.apiService.domain

data class RecommendArticle(
    val code: Int,
    val data: List<Data>,
    val message: String,
    val success: Boolean
){
    data class Data(
        val articleType: Any,
        val avatar: String,
        val covers: List<String>,
        val createTime: String,
        val id: String,
        val labels: List<String>,
        val nickname: String,
        val state: Any,
        val thumbUp: Int,
        val title: String,
        val userId: String,
        val viewCount: String,
        val vip: Boolean
    )
}

