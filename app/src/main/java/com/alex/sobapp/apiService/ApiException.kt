package com.alex.sobapp.apiService

data class ApiException(val code: Int, override val message: String?) : RuntimeException() {

}