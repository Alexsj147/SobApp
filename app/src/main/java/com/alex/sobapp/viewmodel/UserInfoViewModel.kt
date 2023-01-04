package com.alex.sobapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alex.sobapp.apiService.domain.UserInfo
import com.alex.sobapp.repository.UserInfoRepository
import com.alex.sobapp.utils.LoadState
import kotlinx.coroutines.launch
import java.lang.Exception
import java.lang.NullPointerException

class UserInfoViewModel : ViewModel() {

    private val userInfoRepository by lazy {
        UserInfoRepository()
    }

    val contentValue = MutableLiveData<UserInfo>()
    val loadState = MutableLiveData<LoadState>()

    private fun getUserInfo(userId: String) {
        viewModelScope.launch {
            try {
                val userInfo = userInfoRepository.getUserInfo(userId)
                contentValue.postValue(userInfo)
                loadState.postValue(LoadState.SUCCESS)
            } catch (e: Exception) {
                if (e is NullPointerException) {
                    loadState.postValue(LoadState.ERROR)
                } else {
                    loadState.postValue(LoadState.ERROR)
                }
            }
        }
    }

    fun loadUserInfo(userId: String) {
        this.getUserInfo(userId)
    }
}