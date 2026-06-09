package com.example.news.data.repository

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    // Здесь будут зависимости: API, TokenManager и т.д.
) : AuthRepository {

    override suspend fun login(email: String, password: String): Result<Unit> {
        // TODO: Реализация
        return Result.success(Unit)
    }

    override suspend fun register(username: String, password: String): Result<Unit> {
        // TODO: Реализация
        return Result.success(Unit)
    }
}