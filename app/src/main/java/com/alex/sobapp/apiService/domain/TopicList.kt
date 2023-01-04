package com.alex.sobapp.apiService.domain

data class TopicList(
    val code: Int,
    val `data`: List<Data>,
    val message: String,
    val success: Boolean
){
    data class Data(
        val contentCount: Int,
        val cover: String,
        val description: String,
        val followCount: Int,
        val hasFollowed: Boolean,
        val id: String,
        val topicName: String
    )
}

