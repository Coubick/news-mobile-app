package com.example.news.data.api

import com.example.news.data.dto.NewsDto
import com.example.news.data.dto.NewsListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET("news")
    suspend fun getNewsList(
        @Query("city_id") cityId: Long? = null, // если null - то без фильтра по городам
        @Query("sphere_id") sphereId: Long? = null, // если null - то без фильтра по сферам
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 0
    ): Response<NewsListResponse>
}