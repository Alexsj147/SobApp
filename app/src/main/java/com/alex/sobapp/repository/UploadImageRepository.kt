package com.alex.sobapp.repository

import com.alex.sobapp.utils.RetrofitClient
import okhttp3.MultipartBody

class UploadImageRepository {
    suspend fun uploadImage(imageFile: MultipartBody.Part) =
        RetrofitClient.apiService.uploadImage(imageFile)
}