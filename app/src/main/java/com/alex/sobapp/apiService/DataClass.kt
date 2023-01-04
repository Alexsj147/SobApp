package com.alex.sobapp.apiService

data class DataClass<T>(
    val code: Int,
    val data: T,
    val message: String,
    val success: Boolean
) {
    companion object {
        const val CODE_SUCCESS = 10000
    }

    fun apiData(): T {
        //如果成功，返回数据，否则抛出异常
        if (code == CODE_SUCCESS) {
            return data
        } else {
            throw ApiException(code, message)
        }
    }
}