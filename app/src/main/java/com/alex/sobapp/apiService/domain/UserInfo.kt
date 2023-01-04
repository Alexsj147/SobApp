package com.alex.sobapp.apiService.domain

data class UserInfo(
    val area: String,
    val avatar: String,
    val company: String,
    val nickname: String,
    val position: String,
    val sign: String,
    val userId: String,
    val vip: Boolean
)