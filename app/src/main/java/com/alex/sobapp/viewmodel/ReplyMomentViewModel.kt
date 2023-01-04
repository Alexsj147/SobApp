package com.alex.sobapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alex.sobapp.apiService.domain.MomentComment
import com.alex.sobapp.apiService.domain.SubComment
import com.alex.sobapp.repository.ReplyMomentRepository
import kotlinx.coroutines.launch
import java.lang.Exception

class ReplyMomentViewModel : ViewModel() {

    private val replyMomentRepository by lazy {
        ReplyMomentRepository()
    }

    //发表评论(评论动态)
    fun replyMoment(momentComment: MomentComment) {
        viewModelScope.launch {
            try {
                val replyMomentResult = replyMomentRepository.replyMoment(momentComment)
                Log.d("myLog", "replyMomentResult is $replyMomentResult")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    //回复评论
    fun replySubComment(subComment: SubComment) {
        viewModelScope.launch {
            //Log.d("myLog","subComment is $subComment")
            try {
                val replySubCommentResult = replyMomentRepository.replySubComment(subComment)
                Log.d("myLog", "replySubCommentResult is $replySubCommentResult")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}