package com.alex.sobapp.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alex.sobapp.repository.UploadImageRepository
import com.alex.sobapp.utils.Constants.TAG
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class UploadImageViewModel : ViewModel() {

    private val uploadImageRepository by lazy {
        UploadImageRepository()
    }

    val contentValue = MutableLiveData<String>()

    fun uploadImage(imagePath: String) {
        //创建requestBody，用于封装构建RequestBody
        val file = File(imagePath)
        val mediaType = "image/jpeg".toMediaTypeOrNull()
        //Log.d(TAG, "mediaType is $mediaType")
        val requestFile = file.asRequestBody(mediaType)
        //Log.d(TAG, "requestFile is $requestFile")
        val multipartBody =
            MultipartBody.Part.createFormData("image", file.name.trim(), requestFile)
        //Log.d(TAG, "file.name is ${file.name.trim()}")
        viewModelScope.launch {
            val result = uploadImageRepository.uploadImage(multipartBody)
            //Log.d(TAG, "uploadImage: result is $result")
            val imageUrl = result.data
            Log.d(TAG, "uploadImage: 返回的imageUrl is $imageUrl")
            contentValue.postValue(imageUrl)
        }
    }
}
