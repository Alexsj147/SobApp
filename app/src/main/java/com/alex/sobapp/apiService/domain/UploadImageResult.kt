package com.alex.sobapp.apiService.domain

data class UploadImageResult(
    val code: Int,
    val data: String,
    val message: String,
    val success: Boolean
)