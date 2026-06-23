package com.example.news.domain.repository

import com.example.news.data.dto.UserDto
import com.example.news.utils.Result

interface UserRepository {
    suspend fun getCurrentUserData(): Result<UserDto>
}