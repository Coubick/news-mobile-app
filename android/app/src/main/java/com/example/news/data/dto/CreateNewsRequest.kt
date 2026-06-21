package com.example.news.data.dto

import com.google.gson.annotations.SerializedName

data class CreateNewsRequest(
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("city_id") val cityId: Long?,
    @SerializedName("sphere_id") val sphereId: Long?,
    @SerializedName("image_url") val imageUrl: String?
)