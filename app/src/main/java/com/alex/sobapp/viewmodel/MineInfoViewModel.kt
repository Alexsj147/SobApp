package com.alex.sobapp.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alex.sobapp.apiService.domain.*
import com.alex.sobapp.repository.MineInfoRepository
import com.alex.sobapp.utils.Constants.TAG
import com.alex.sobapp.utils.LoadState
import kotlinx.coroutines.launch
import java.lang.Exception

class MineInfoViewModel : ViewModel() {

    private val mineInfoRepository by lazy {
        MineInfoRepository()
    }

    val loadState = MutableLiveData<LoadState>()
    val articleCommentValue = MutableLiveData<MutableList<MineArticleComment.Content>>()
    val dynamicCommentValue = MutableLiveData<MutableList<MineDynamicComment.Content>>()
    val wendaListValue = MutableLiveData<MutableList<MineWendaList.Content>>()
    val atMeInfoValue = MutableLiveData<MutableList<MineAtMeInfo.Content>>()
    val thumbUpListValue = MutableLiveData<MutableList<MineThumbUpList.Content>>()
    val systemInfoValue = MutableLiveData<MutableList<SystemInfo.Content>>()

    //获取文章评论
    private fun getMineArticleComment(page: Int) {
        viewModelScope.launch {
            try {
                val mineArticleComment = mineInfoRepository.getMineArticleComment(page)
                //Log.d(TAG, "mineArticleComment is $mineArticleComment")
                val content = mineArticleComment.content
                val oldValue = articleCommentValue.value ?: mutableListOf()
                oldValue.addAll(content)
                articleCommentValue.postValue(oldValue)
                loadState.postValue(LoadState.SUCCESS)
            } catch (e: Exception) {
                e.printStackTrace()
                loadState.postValue(LoadState.ERROR)
            }

        }
    }

    //获取动态评论
    private fun getDynamicComment(page: Int) {
        viewModelScope.launch {
            try {
                val mineDynamicComment = mineInfoRepository.getMineDynamicComment(page)
                //Log.d(TAG, "mineDynamicComment is $mineDynamicComment")
                val content = mineDynamicComment.content
                val oldValue = dynamicCommentValue.value ?: mutableListOf()
                oldValue.addAll(content)
                dynamicCommentValue.postValue(oldValue)
                loadState.postValue(LoadState.SUCCESS)
            } catch (e: Exception) {
                e.printStackTrace()
                loadState.postValue(LoadState.ERROR)
            }
        }
    }

    //获取问答列表
    private fun getMineWendaList(page: Int) {
        viewModelScope.launch {
            try {
                val mineWendaList = mineInfoRepository.getMineWendaList(page)
                val content = mineWendaList.content
                val oldValue = wendaListValue.value ?: mutableListOf()
                oldValue.addAll(content)
                wendaListValue.postValue(oldValue)
                loadState.postValue(LoadState.SUCCESS)
            } catch (e: Exception) {
                e.printStackTrace()
                loadState.postValue(LoadState.ERROR)
            }
        }
    }

    //获取回复我的
    private fun getMineAtMeInfo(page: Int) {
        viewModelScope.launch {
            try {
                val mineAtMeInfo = mineInfoRepository.getMineAtMeInfo(page)
                val content = mineAtMeInfo.content
                val oldValue = atMeInfoValue.value ?: mutableListOf()
                oldValue.addAll(content)
                atMeInfoValue.postValue(oldValue)
                loadState.postValue(LoadState.SUCCESS)
            } catch (e: Exception) {
                e.printStackTrace()
                loadState.postValue(LoadState.ERROR)
            }
        }
    }

    //获取点赞列表
    private fun getThumbUpList(page: Int) {
        viewModelScope.launch {
            try {
                val mineThumbUpList = mineInfoRepository.getMineThumbUpList(page)
                val content = mineThumbUpList.content
                val oldValue = thumbUpListValue.value ?: mutableListOf()
                oldValue.addAll(content)
                thumbUpListValue.postValue(oldValue)
                loadState.postValue(LoadState.SUCCESS)
            } catch (e: Exception) {
                e.printStackTrace()
                loadState.postValue(LoadState.ERROR)
            }
        }
    }

    //获取系统消息
    private fun getSystemInfo(page: Int) {
        viewModelScope.launch {
            try {
                val systemInfo = mineInfoRepository.getSystemInfo(page)
                val content = systemInfo.content
                val oldValue = systemInfoValue.value ?: mutableListOf()
                oldValue.addAll(content)
                systemInfoValue.postValue(oldValue)
                loadState.postValue(LoadState.SUCCESS)
            } catch (e: Exception) {
                e.printStackTrace()
                loadState.postValue(LoadState.ERROR)
            }
        }
    }

    fun loadMineArticleComment(page: Int) {
        this.getMineArticleComment(page)
    }

    fun loadDynamicComment(page: Int) {
        this.getDynamicComment(page)
    }

    fun loadWendaList(page: Int) {
        this.getMineWendaList(page)
    }

    fun loadAtMeInfo(page: Int) {
        this.getMineAtMeInfo(page)
    }

    fun loadThumbUpList(page: Int) {
        this.getThumbUpList(page)
    }

    fun loadSystemInfo(page: Int){
        this.getSystemInfo(page)
    }
}