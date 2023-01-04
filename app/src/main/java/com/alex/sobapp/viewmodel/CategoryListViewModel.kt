package com.alex.sobapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alex.sobapp.apiService.domain.CategoryList
import com.alex.sobapp.repository.CategoryRepository
import com.alex.sobapp.utils.LoadState
import kotlinx.coroutines.launch
import java.lang.Exception
import java.lang.NullPointerException

class CategoryListViewModel : ViewModel() {

    private val categoryRepository by lazy {
        CategoryRepository()
    }

    val contentValue = MutableLiveData<MutableList<CategoryList.Data>>()
    val loadState = MutableLiveData<LoadState>()

    private fun getCategoryList() {
        viewModelScope.launch {
            try {
                val oldValue: MutableList<CategoryList.Data> =
                    contentValue.value ?: mutableListOf()
                val recommendItem = CategoryList.Data(
                    "推荐", "2021-07-02",
                    "推荐内容", "0", 0, "recommend", "null"
                )
                oldValue.clear()
                oldValue.add(0, recommendItem)
                val categoryList = categoryRepository.getCategoryList()
                val data = categoryList.data
                oldValue.addAll(data)
                contentValue.postValue(oldValue)
                if (data.isEmpty()) {
                    loadState.postValue(LoadState.EMPTY)
                } else {
                    loadState.postValue(LoadState.SUCCESS)
                }
            } catch (e: Exception) {
                if (e is NullPointerException) {
                    loadState.postValue(LoadState.ERROR)
                } else {
                    loadState.postValue(LoadState.ERROR)
                }
            }

        }
    }

    fun loadCategory() {
        loadState.postValue(LoadState.LOADING)
        this.getCategoryList()
    }
}