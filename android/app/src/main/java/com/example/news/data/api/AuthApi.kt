package com.example.news.data.api

import com.example.news.data.dto.LoginRequest
import com.example.news.data.dto.RegisterRequest
import com.example.news.data.dto.TokenResponse
import com.example.news.data.dto.UserDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface AuthApi {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<TokenResponse>

    @POST("auth/register")
    suspend fun register(@Body userDto: RegisterRequest) : Response<TokenResponse>

    @GET("users/me")
    suspend fun getCurrentUser(): Response<UserDto>

    @PUT("users/me")
    suspend fun updateUser(@Body request: UserUpdateRequest): Response<UserDto>
}

data class UserUpdateRequest(
    val username: String? = null,
    val password: String? = null,
    val avatarUrl: String? = null
)