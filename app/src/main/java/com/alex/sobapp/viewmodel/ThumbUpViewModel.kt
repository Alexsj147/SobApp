package com.alex.sobapp.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alex.sobapp.apiService.domain.ThumbUpResult
import com.alex.sobapp.repository.ThumbUpRepository
import kotlinx.coroutines.launch

class ThumbUpViewModel : ViewModel() {

    private val thumbUpRepository by lazy {
        ThumbUpRepository()
    }

    val result = MutableLiveData<ThumbUpResult>()

    fun thumbUp(momentId: String) {
        viewModelScope.launch {
            val thumbUpResult = thumbUpRepository.getThumbUpResult(momentId)
            //Log.d("myLog", "点赞返回值：$thumbUpResult")
            result.postValue(thumbUpResult)
        }
    }
}