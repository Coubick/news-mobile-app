package com.example.news.data.api

import com.example.news.data.dto.FilterItemDto
import retrofit2.Response
import retrofit2.http.GET

interface NewsFilterOptionsApi {
    @GET("cities")
    suspend fun getAllCities(): Response<List<FilterItemDto>>

    @GET("spheres")
    suspend fun getAllSpheres(): Response<List<FilterItemDto>>
}