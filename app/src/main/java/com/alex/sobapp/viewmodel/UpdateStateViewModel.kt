package com.alex.sobapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alex.sobapp.repository.UpdateStateRepository
import kotlinx.coroutines.launch
import java.lang.Exception

class UpdateStateViewModel : ViewModel() {

    private val updateStateRepository by lazy {
        UpdateStateRepository()
    }

    fun updateDynamicState(msgId: String) {
        viewModelScope.launch {
            try {
                val updateDynamicStateResult = updateStateRepository.updateDynamicState(msgId)
                Log.d("myLog", updateDynamicStateResult.message)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}