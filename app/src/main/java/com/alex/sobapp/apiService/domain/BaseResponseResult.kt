package com.alex.sobapp.apiService.domain

data class BaseResponseResult(
    val code: Int,
    val data: Any,
    val message: String,
    val success: Boolean
)