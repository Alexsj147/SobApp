package com.alex.sobapp.apiService.domain

data class UnreadInfo(
    val articleMsgCount: Int,
    val atMsgCount: Int,
    val momentCommentCount: Int,
    val shareMsgCount: Int,
    val systemMsgCount: Int,
    val thumbUpMsgCount: Int,
    val wendaMsgCount: Int
)