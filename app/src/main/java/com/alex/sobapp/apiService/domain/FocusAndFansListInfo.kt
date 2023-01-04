package com.alex.sobapp.apiService.domain

data class FocusAndFansListInfo(
    val currentPage: Int,
    val hasNext: Boolean,
    val hasPre: Boolean,
    val list: List<ListItem>,
    val pageSize: Int,
    val total: Int,
    val totalPage: Int
) {
    data class ListItem(
        val avatar: String,
        val nickname: String,
        val relative: Int,
        val sign: String,
        val userId: String,
        val vip: Boolean
    )
}
