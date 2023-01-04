package com.alex.sobapp.apiService.domain

data class MoYuCategory(
    val code: Int,
    val `data`: List<Data>,
    val message: String,
    val success: Boolean
) {
    data class Data(
        val contentCount: Int,
        val cover: Any,
        val createTime: Any,
        val description: Any,
        val followCount: Int,
        val id: String,
        val order: Int,
        val topicName: String,
        val updateTime: Any
    )
}


