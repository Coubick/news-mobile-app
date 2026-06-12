package com.example.news.utils

sealed interface Result {
    data class Success(val msg: String="") : Result
    data class Failure(val msg: String="") : Result
}