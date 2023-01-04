package com.alex.sobapp.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alex.sobapp.apiService.domain.CourseList
import com.alex.sobapp.repository.SchoolRepository
import com.alex.sobapp.utils.Constants
import com.alex.sobapp.utils.LoadState
import kotlinx.coroutines.launch
import java.lang.Exception
import java.lang.NullPointerException

class SchoolViewModel : ViewModel() {

    private val schoolRepository by lazy {
        SchoolRepository()
    }

    //当前页码
    private var currentPage = Constants.DEFAULT_PAGE
    private var isLoadMore: Boolean = false

    val courseListValue = MutableLiveData<MutableList<CourseList.Course>>()
    val loadState = MutableLiveData<LoadState>()

    private fun getCourseList(page: Int) {
        viewModelScope.launch {
            try {
                val courseListResult = schoolRepository.getCourseList(page)
                val oldValue: MutableList<CourseList.Course> =
                    courseListValue.value ?: arrayListOf()
                val list = courseListResult.list
                if (!isLoadMore) {
                    oldValue.clear()
                }
                oldValue.addAll(list)
                courseListValue.postValue(oldValue)
                if (list.isEmpty()) {
                    loadState.value =
                        if (isLoadMore) LoadState.LOADING_MORE_EMPTY else LoadState.EMPTY
                } else {
                    loadState.value =
                        if (isLoadMore) LoadState.LOADING_MORE_SUCCESS else LoadState.SUCCESS
                }
                //Log.d("myLog", "courseListValue is ${courseListValue.value}")

            } catch (e: Exception) {
                currentPage--
                e.printStackTrace()
                if (e is NullPointerException) {
                    //没有更多的时候，会空指针
                    loadState.value = LoadState.EMPTY
                } else {
                    loadState.value =
                        if (isLoadMore) LoadState.LOADING_MORE_ERROR else LoadState.ERROR
                }
            }
        }
    }

    fun loadCourseList() {
        isLoadMore = false
        loadState.value = LoadState.LOADING
        currentPage = Constants.DEFAULT_PAGE
        this.getCourseList(currentPage)
    }
}