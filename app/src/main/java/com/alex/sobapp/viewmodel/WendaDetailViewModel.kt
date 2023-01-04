package com.alex.sobapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alex.sobapp.apiService.domain.RelatedQuestionList
import com.alex.sobapp.apiService.domain.WendaAnswer
import com.alex.sobapp.apiService.domain.WendaDetail
import com.alex.sobapp.repository.WendaDetailRepository
import com.alex.sobapp.utils.Constants
import com.alex.sobapp.utils.LoadState
import kotlinx.coroutines.launch

class WendaDetailViewModel : ViewModel() {

    private val wendaDetailRepository by lazy {
        WendaDetailRepository()
    }

    val wendaDetailValue = MutableLiveData<WendaDetail>()
    val loadState = MutableLiveData<LoadState>()
    val wendaAnswerValue = MutableLiveData<MutableList<WendaAnswer.Data>>()
    val relatedQuestionValue = MutableLiveData<RelatedQuestionList>()

    //当前页码
    private var currentPage = Constants.DEFAULT_PAGE
    private var isLoadMore: Boolean = false

    private fun getWendaDetail(wendaId: String) {
        viewModelScope.launch {
            try {
                val wendaDetailResult = wendaDetailRepository.getWendaDetail(wendaId)
                wendaDetailValue.postValue(wendaDetailResult)
                loadState.postValue(LoadState.SUCCESS)
            } catch (e: Exception) {
                e.printStackTrace()
                loadState.postValue(LoadState.ERROR)
            }
        }
    }

    private fun getWendaAnswerList(wendaId: String, page: Int) {
        viewModelScope.launch {
            try {
                val wendaAnswerListResult = wendaDetailRepository.getWendaAnswerList(wendaId, page)
                //Log.d("myLog","wendaAnswerListResult is $wendaAnswerListResult")
                val data = wendaAnswerListResult.data
                val oldValue = wendaAnswerValue.value ?: arrayListOf()
                oldValue.addAll(data)
                wendaAnswerValue.postValue(oldValue)
                loadState.postValue(LoadState.SUCCESS)
            } catch (e: Exception) {
                e.printStackTrace()
                loadState.postValue(LoadState.ERROR)

            }
        }
    }

    private fun getRelatedQuestion(wendaId: String, size: Int) {
        viewModelScope.launch {
            try {
                val relatedQuestionResult = wendaDetailRepository.getRelatedQuestion(wendaId, size)
                //Log.d("myLog", "relatedQuestionResult is $relatedQuestionResult")
                relatedQuestionValue.postValue(relatedQuestionResult)
                loadState.postValue(LoadState.SUCCESS)
            } catch (e: Exception) {
                e.printStackTrace()
                loadState.postValue(LoadState.ERROR)
            }
        }
    }

    fun loadWendaDetail(wendaId: String) {
        loadState.postValue(LoadState.LOADING)
        this.getWendaDetail(wendaId)
    }

    fun loadWendaAnswer(wendaId: String) {
        loadState.postValue(LoadState.LOADING)
        currentPage = Constants.DEFAULT_PAGE
        this.getWendaAnswerList(wendaId, currentPage)
    }

    fun loadRelatedQuestion(wendaId: String) {
        loadState.postValue(LoadState.LOADING)
        this.getRelatedQuestion(wendaId, Constants.RELATED_QUESTION_NUM)
    }
}