package com.alex.sobapp.apiService.domain

/**
 * content为评论内容，momentId为动态Id，targetUserId是被评论内容的用户Id，commentId为被评论内容的Id
 */
data class SubComment(
    val content: String,
    val momentId: String,
    val targetUserId: String,
    val commentId: String
) {
}