package com.example.news.data.repository

import com.example.news.data.api.AuthApi
import com.example.news.data.dto.LoginRequest
import com.example.news.data.dto.RegisterRequest
import com.example.news.data.local.TokenManager
import com.example.news.domain.repository.AuthRepository
import javax.inject.Inject
import javax.inject.Singleton
import com.example.news.utils.Result

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val tokenManager: TokenManager
) : AuthRepository {

    override suspend fun login(username: String, password: String): Result<Unit> {
        return try {
            val response = authApi.login(LoginRequest(username, password))

            if (response.isSuccessful && response.body() != null){
                val token = response.body()!!.accessToken
                tokenManager.saveToken(token)
                Result.Success(Unit)
            } else {
                val errorCode = response.code()
                Result.Failure("Failure with: $errorCode")
            }
        } catch (e: Exception){
            Result.Failure(("Нет подключения к сети: ${e.message}"))
        }
    }

    override suspend fun register(username: String, password: String): Result<Unit> {
        return try {
            val response = authApi.register(RegisterRequest(username, password))
            if (response.isSuccessful) {
                val token = response.body()!!.accessToken
                tokenManager.saveToken(token)
                Result.Success(Unit)
            } else {
                val errorCode = response.code()
                Result.Failure("Failure with: $errorCode")
            }
        } catch (e: Exception){
            Result.Failure("No connection: ${e.message}")
        }
    }
}