package com.alex.sobapp.apiService.domain

data class RecommendList(
    val currentPage: Int,
    val hasNext: Boolean,
    val hasPre: Boolean,
    val list: List<Info>,
    val pageSize: Int,
    val total: Int,
    val totalPage: Int
) {
    data class Info(
        val avatar: String,
        val covers: List<String>,
        val createTime: String,
        val id: String,
        val nickName: String,
        val thumbUp: Int,
        val title: String,
        val type: Int,
        val userId: String,
        val viewCount: Int,
        val vip: Boolean
    )
}


