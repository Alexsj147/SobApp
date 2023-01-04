package com.alex.sobapp.apiService.domain

data class MoYuInfoList(
    val currentPage: Int,
    val hasNext: Boolean,
    val hasPre: Boolean,
    val list: List<MoYuItem>,
    val pageSize: Int,
    val total: Int,
    val totalPage: Int
) {
    data class MoYuItem(
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


