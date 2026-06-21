package com.example.news.utils

sealed interface Result<T> {
    data class Success<T>(val data: T?, val msg: String = "") : Result<T>
    data class Failure<T>(val msg: String="") : Result<T>
}