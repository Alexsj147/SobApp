package com.alex.sobapp.apiService.domain

data class CategoryList(
    val code: Int,
    val `data`: List<Data>,
    val message: String,
    val success: Boolean
) {
    data class Data(
        val categoryName: String,
        val createTime: String,
        val description: String,
        val id: String,
        val order: Int,
        val pyName: String,
        val updateTime: Any
    )
}

