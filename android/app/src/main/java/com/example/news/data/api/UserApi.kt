package com.example.news.data.api

import com.example.news.data.dto.UserDto
import retrofit2.Response
import retrofit2.http.GET

interface UserApi {
    @GET("users/me")
    suspend fun getCurrentUserData(): Response<UserDto>
}