package com.alex.sobapp.apiService.domain

data class ReleaseDynamicResult(
    val code: Int,
    val data: ReleaseResult,
    val message: String,
    val success: Boolean
) {
    data class ReleaseResult(
        val avatar: String,
        val commentCount: Int,
        val company: String,
        val content: String,
        val createTime: String,
        val id: String,
        val images: List<String>,
        val linkCover: String,
        val linkTitle: String,
        val linkUrl: String,
        val nickname: String,
        val position: String,
        val thumbUpCount: Int,
        val thumbUpList: List<String>,
        val topicId: String,
        val topicName: String,
        val userId: String,
        val vip: Boolean
    )
}