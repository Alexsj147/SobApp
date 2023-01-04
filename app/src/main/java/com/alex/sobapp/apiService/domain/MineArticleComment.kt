package com.alex.sobapp.apiService.domain

data class MineArticleComment(
    val content: List<Content>,
    val first: Boolean,
    val last: Boolean,
    val number: Int,
    val numberOfElements: Int,
    val pageable: Pageable,
    val size: Int,
    val sort: SortX,
    val totalElements: Int,
    val totalPages: Int
){
    data class Content(
        val _id: String,
        val articleId: String,
        val avatar: String,
        val bUid: String,
        val content: String,
        val createTime: String,
        val hasRead: String,
        val nickname: String,
        val timeText: Any,
        val title: String,
        val uid: String
    )
    data class Pageable(
        val offset: Int,
        val pageNumber: Int,
        val pageSize: Int,
        val paged: Boolean,
        val sort: Sort,
        val unpaged: Boolean
    ){
        data class Sort(
            val sorted: Boolean,
            val unsorted: Boolean
        )
    }
    data class SortX(
        val sorted: Boolean,
        val unsorted: Boolean
    )
}








