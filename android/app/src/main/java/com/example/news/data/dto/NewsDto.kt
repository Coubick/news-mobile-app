package com.example.news.data.dto

import com.example.news.domain.model.NewsItem
import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.TimeUnit

data class NewsDto(
    @SerializedName("id") val id: Long,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("image_url") val imageUrl: String?,
    @SerializedName("user_id") val userId: Long,
    @SerializedName("city_id") val cityId: Long,
    @SerializedName("sphere_id") val sphereId: Long,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String,
    @SerializedName("user") val user: UserDto,
    @SerializedName("city") val city: FilterItemDto,
    @SerializedName("sphere") val sphere: FilterItemDto
)

data class FilterItemDto(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String
)

fun NewsDto.toNewsItem(): NewsItem {
    return NewsItem(
        id = id,
        title = title,
        description = description,
        imageUrl = imageUrl,
        authorName = user.username,
        authorAvatarUrl = user.avatarUrl,
        cityName = city.name,
        sphereName = sphere.name,
        createdAt = formatDateTime(createdAt)
    )
}

private fun formatDateTime(isoDate: String): String {
    return try {
        val cleanDate = isoDate.substringBefore(".")
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val date = format.parse(cleanDate) ?: return isoDate

        val now = System.currentTimeMillis()
        val diff = now - date.time

        when {
            diff < TimeUnit.MINUTES.toMillis(1) -> "только что"
            diff < TimeUnit.HOURS.toMillis(1) -> {
                val minutes = TimeUnit.MILLISECONDS.toMinutes(diff)
                "$minutes мин. назад"
            }

            diff < TimeUnit.DAYS.toMillis(1) -> {
                val hours = TimeUnit.MILLISECONDS.toHours(diff)
                "$hours ч. назад"
            }

            diff < TimeUnit.DAYS.toMillis(7) -> {
                val days = TimeUnit.MILLISECONDS.toDays(diff)
                "$days дн. назад"
            }

            else -> {
                val displayFormat = SimpleDateFormat("d MMM yyyy", Locale("ru"))
                displayFormat.format(date)
            }
        }
    } catch (e: Exception) {
        isoDate
    }
}