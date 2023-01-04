package com.alex.sobapp.apiService.domain

data class TokenInfo(
    val code: Int,
    val data: TokenData,
    val message: String,
    val success: Boolean
) {
    data class TokenData(
        val avatar: String,
        val fansCount: Int,
        val followCount: Int,
        val id: String,
        val isVip: String,
        val lev: Int,
        val nickname: String,
        val roles: Int,
        val token: String
    )
}
