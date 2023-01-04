package com.alex.sobapp.apiService.domain

data class UpdateStateResult(
    val code: Int,
    val `data`: Any,
    val message: String,
    val success: Boolean
)