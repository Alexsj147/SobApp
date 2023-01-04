package com.alex.sobapp.apiService.domain

import com.alex.sobapp.apiService.ApiException

data class LoginResponse(
    val code: Int,
    val data: Any,
    val message: String,
    val success: Boolean
)