package com.alex.sobapp.apiService.domain

data class DynamicBody(
    val content: String,
    val linkUrl: String,
    val images: List<String>,
    val topicId: String
)