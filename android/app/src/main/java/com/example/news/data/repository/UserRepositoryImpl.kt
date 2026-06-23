package com.example.news.data.repository

import com.example.news.data.api.UserApi
import com.example.news.data.dto.UserDto
import com.example.news.domain.repository.UserRepository
import com.example.news.utils.Result
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val userApi: UserApi
) : UserRepository {
    override suspend fun getCurrentUserData(): Result<UserDto> {
        return try {
            val data = userApi.getCurrentUserData()
            if (data.isSuccessful){
                Result.Success(data = data.body(), msg = "Success")
            } else {
                Result.Failure(msg = "Failure: ${data.code()}")
            }
        } catch (e: Exception){
            Result.Failure(msg = "Exception: ${e.message}")
        }
    }
}