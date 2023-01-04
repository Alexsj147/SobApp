package com.alex.sobapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alex.sobapp.apiService.domain.CourseChapter
import com.alex.sobapp.apiService.domain.CourseDetail
import com.alex.sobapp.apiService.domain.VideoResult
import com.alex.sobapp.repository.CourseRepository
import com.alex.sobapp.utils.LoadState
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.xml.transform.Source

class CourseViewModel : ViewModel() {

    private val courseRepository by lazy {
        CourseRepository()
    }

    val courseDetailValue = MutableLiveData<CourseDetail>()
    val courseChapterValue = MutableLiveData<MutableList<CourseChapter.CourseChapterItem>>()
    val videoValue = MutableLiveData<VideoResult>()
    val loadState = MutableLiveData<LoadState>()

    private fun getCourseDetail(courseId: String) {
        viewModelScope.launch {
            try {
                val courseDetailResult = courseRepository.getCourseDetail(courseId)
                courseDetailValue.postValue(courseDetailResult)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun getCourseChapter(courseId: String) {
        viewModelScope.launch {
            try {
                val courseChapterResult = courseRepository.getCourseChapter(courseId)
                courseChapterValue.postValue(courseChapterResult)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun getVideo(videoId: String) {
        viewModelScope.launch {
            try {
                val videoResult = courseRepository.getVideo(videoId)
                videoValue.postValue(videoResult)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun loadCourseDetail(courseId: String) {
        this.getCourseDetail(courseId)
    }

    fun loadCourseChapter(courseId: String) {
        this.getCourseChapter(courseId)
    }

    fun loadVideo(videoId: String) {
        this.getVideo(videoId)
    }
}