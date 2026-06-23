package com.example.news.domain.repository
import com.example.news.utils.Result
interface AuthRepository {
    suspend fun login(username: String, password: String): Result<Unit>
    suspend fun register(username: String, password: String): Result<Unit>
}