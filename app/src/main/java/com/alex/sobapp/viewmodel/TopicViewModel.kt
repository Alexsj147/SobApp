package com.alex.sobapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alex.sobapp.apiService.domain.TopicList
import com.alex.sobapp.repository.TopicRepository
import com.alex.sobapp.utils.LoadState
import kotlinx.coroutines.launch

class TopicViewModel : ViewModel() {

    private val topicRepository by lazy {
        TopicRepository()
    }

    val contentValue = MutableLiveData<MutableList<TopicList.Data>>()
    val loadState = MutableLiveData<LoadState>()

    private fun getTopic() {
        viewModelScope.launch {
            val topicList = topicRepository.getTopicListInfo().data
            val oldValue: MutableList<TopicList.Data> = contentValue.value ?: mutableListOf()
            oldValue.clear()
            oldValue.addAll(topicList)
            contentValue.postValue(oldValue)
        }
    }

    fun loadTopicList() {
        this.getTopic()
    }
}