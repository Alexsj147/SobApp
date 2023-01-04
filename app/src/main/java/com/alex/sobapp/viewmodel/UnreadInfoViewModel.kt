package com.alex.sobapp.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alex.sobapp.apiService.domain.BaseResponseResult
import com.alex.sobapp.apiService.domain.UnreadInfo
import com.alex.sobapp.repository.UnreadInfoRepository
import com.alex.sobapp.utils.Constants.TAG
import com.alex.sobapp.utils.LoadState
import kotlinx.coroutines.launch
import java.lang.Exception

class UnreadInfoViewModel : ViewModel() {

    private val unreadInfoRepository by lazy {
        UnreadInfoRepository()
    }

    val unreadInfoValue = MutableLiveData<UnreadInfo>()
    val oneStepReadValue = MutableLiveData<BaseResponseResult>()
    val loadState = MutableLiveData<LoadState>()

    fun getUnreadInfo() {
        viewModelScope.launch {
            try {
                val unreadInfo = unreadInfoRepository.getUnreadInfo()
                unreadInfoValue.postValue(unreadInfo)
                loadState.postValue(LoadState.SUCCESS)
                Log.d(TAG, "unreadInfo is $unreadInfo")
            } catch (e: Exception) {
                e.printStackTrace()
                loadState.postValue(LoadState.ERROR)
            }

        }
    }

    fun oneStepRead() {
        viewModelScope.launch {
            try {
                val oneStepReadResult = unreadInfoRepository.oneStepRead()
                oneStepReadValue.postValue(oneStepReadResult)
                Log.d(TAG, "oneStepReadResult is $oneStepReadResult")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}