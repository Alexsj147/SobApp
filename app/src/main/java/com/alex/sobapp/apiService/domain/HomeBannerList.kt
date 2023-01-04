package com.alex.sobapp.apiService.domain

data class HomeBannerList(
    val code: Int,
    val data: List<Data>,
    val message: String,
    val success: Boolean
) {
    data class Data(
        val createTime: String,
        val picUrl: String,
        val targetUrl: String,
        val type: String
    )
}
