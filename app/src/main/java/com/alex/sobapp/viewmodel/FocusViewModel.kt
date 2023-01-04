package com.alex.sobapp.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alex.sobapp.repository.FocusRepository
import com.alex.sobapp.utils.Constants.TAG
import kotlinx.coroutines.launch

class FocusViewModel : ViewModel() {

    private val focusRepository by lazy {
        FocusRepository()
    }

    val addResult = MutableLiveData<String>()
    val deleteResult = MutableLiveData<String>()

    fun addFocus(userId: String) {
        viewModelScope.launch {
            val addFocusResult = focusRepository.addFocus(userId)
            Log.d(TAG, "addFocus: ${addFocusResult.success},${addFocusResult.message}")
            addResult.postValue(addFocusResult.message)
        }
    }

    fun deleteFocus(userId: String){
        viewModelScope.launch {
            val deleteFocusResult = focusRepository.deleteFocus(userId)
            Log.d(TAG, "addFocus: ${deleteFocusResult.success},${deleteFocusResult.message}")
            deleteResult.postValue(deleteFocusResult.message)
        }
    }
}