package com.example.news.data.repository

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<Unit>
    suspend fun register(username: String, password: String): Result<Unit>
}