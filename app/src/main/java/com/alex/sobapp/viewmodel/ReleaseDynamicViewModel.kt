package com.alex.sobapp.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alex.sobapp.apiService.domain.DynamicBody
import com.alex.sobapp.repository.ReleaseDynamicRepository
import kotlinx.coroutines.launch

class ReleaseDynamicViewModel : ViewModel() {

    private val releaseDynamicRepository by lazy {
        ReleaseDynamicRepository()
    }

    val isSucceed = MutableLiveData<Boolean>()

    fun getReleaseResult(dynamicBody: DynamicBody) {
        viewModelScope.launch {
            val result = releaseDynamicRepository.releaseDynamic(dynamicBody)
            Log.d("myLog", "发布动态返回值: $result")
            isSucceed.postValue(result.success)
        }
    }
}