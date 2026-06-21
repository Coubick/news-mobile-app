package com.example.news.data.dto

import com.google.gson.annotations.SerializedName

data class CloudinarySignatureResponse(
    @SerializedName("signature") val signature: String,
    @SerializedName("timestamp") val timestamp: Int,
    @SerializedName("folder") val folder: String,
    @SerializedName("api_key") val apiKey: String,
    @SerializedName("cloud_name") val cloudName: String
)

data class CloudinaryUploadResponse(
    @SerializedName("secure_url") val secureUrl: String,
    @SerializedName("public_id") val publicId: String,
    @SerializedName("format") val format: String,
    @SerializedName("width") val width: Int,
    @SerializedName("height") val height: Int
)