package com.alex.sobapp.repository

import com.alex.sobapp.apiService.domain.MomentComment
import com.alex.sobapp.apiService.domain.SubComment
import com.alex.sobapp.utils.RetrofitClient

class ReplyMomentRepository {
    //发表评论(评论动态)
    suspend fun replyMoment(momentComment: MomentComment) =
        RetrofitClient.apiService.replyComment(momentComment)

    //回复评论
    suspend fun replySubComment(subComment: SubComment) =
        RetrofitClient.apiService.replySubComment(subComment)
}