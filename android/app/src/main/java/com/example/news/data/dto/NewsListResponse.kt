package com.example.news.data.dto

import com.google.gson.annotations.SerializedName

data class NewsListResponse(
    @SerializedName("total") val total: Int,
    @SerializedName("items") val items: List<NewsDto>
)