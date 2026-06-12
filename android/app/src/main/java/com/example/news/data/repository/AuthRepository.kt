package com.example.news.data.repository
import com.example.news.utils.Result
interface AuthRepository {
    suspend fun login(username: String, password: String): Result
    suspend fun register(username: String, password: String): Result
}