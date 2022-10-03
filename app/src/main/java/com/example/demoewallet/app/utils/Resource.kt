package com.example.demoewallet.app.utils

sealed class Resource<T>(val data: T? = null, val message: String? = null) {
    class Empty<T>(data: T?=null): Resource<T>(data)
    class Success<T>(data: T?): Resource<T>(data)
    class Error<T>(message: String, data: T? = null): Resource<T>(data, message)
}