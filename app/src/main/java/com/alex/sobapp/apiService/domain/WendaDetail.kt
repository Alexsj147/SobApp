package com.alex.sobapp.apiService.domain

data class WendaDetail(
    val answerCount: Int,
    val avatar: String,
    val categoryId: String,
    val categoryName: String,
    val createTime: String,
    val description: String,
    val id: String,
    val isResolve: String,
    val isVip: String,
    val label: Any,
    val labels: List<String>,
    val nickname: String,
    val sob: Int,
    val thumbUp: Int,
    val title: String,
    val userId: String,
    val viewCount: Int
)