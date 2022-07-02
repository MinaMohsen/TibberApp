package com.example.tibberapp.util

sealed class Resource<T>(val data: T? = null, val message: String? = null,val errorCode: Int? = 0) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: String?, errorCode: Int? = 0, data: T? = null) : Resource<T>(data, message, errorCode)
}