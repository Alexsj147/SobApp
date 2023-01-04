package com.alex.sobapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alex.sobapp.apiService.domain.BaseResponseResult
import com.alex.sobapp.repository.VipRepository
import kotlinx.coroutines.launch
import java.lang.Exception

class VipViewModel : ViewModel() {

    private val vipRepository by lazy {
        VipRepository()
    }

    val vipCheckResult = MutableLiveData<BaseResponseResult>()
    val vipGetResult = MutableLiveData<BaseResponseResult>()

    fun checkVipAllowance() {
        viewModelScope.launch {
            try {
                val checkVipAllowanceResult = vipRepository.checkVipAllowance()
                vipCheckResult.postValue(checkVipAllowanceResult)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    fun getVipAllowance() {
        viewModelScope.launch {
            try {
                val getVipAllowanceResult = vipRepository.getVipAllowance()
                vipGetResult.postValue(getVipAllowanceResult)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }
}