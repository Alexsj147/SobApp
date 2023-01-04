package com.alex.sobapp.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alex.sobapp.apiService.domain.TokenInfo
import com.alex.sobapp.base.BaseApplication
import com.alex.sobapp.repository.CheckTokenRepository
import kotlinx.coroutines.launch

class CheckTokenViewModel : ViewModel() {

    private val checkTokenRepository by lazy {
        CheckTokenRepository()
    }

    val resultToken = MutableLiveData<TokenInfo>()


    private fun checkToken() {
        val shp = BaseApplication.getShp()
        //val sobToken = shp?.getString("sob_token", "")
        viewModelScope.launch {
            try {
                val token = checkTokenRepository.checkToken()
                resultToken.postValue(token)
            } catch (e: Exception) {
                Log.d("myLog","exception is $e")
            }
        }
    }

    fun tokenCheck() {
        this.checkToken()
    }
}