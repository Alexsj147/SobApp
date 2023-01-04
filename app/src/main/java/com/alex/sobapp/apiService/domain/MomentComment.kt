package com.alex.sobapp.apiService.domain

/**
 * momentId为动态ID,content为评论内容。
 */
data class MomentComment(
    val momentId: String,
    val content: String
)