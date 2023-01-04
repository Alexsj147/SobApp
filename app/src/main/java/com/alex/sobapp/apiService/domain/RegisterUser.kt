package com.alex.sobapp.apiService.domain

data class RegisterUser(
    val phoneNum: String,
    val password: String,
    val nickname: String
)