package com.alex.sobapp.apiService.domain

data class SystemInfo(
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
        val content: String,
        val exId: Any,
        val exType: String,
        val publishTime: String,
        val state: String,
        val title: String,
        val userId: String
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



