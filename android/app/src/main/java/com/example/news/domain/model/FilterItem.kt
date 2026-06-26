package com.example.news.domain.model

import com.example.news.data.dto.FilterItemDto


// класс для сферы и города (для фильтров важны только их id и имя)
data class FilterItem(
    val id: Long,
    val name: String
)

fun FilterItemDto.toUiModel(): FilterItem = FilterItem(id = id, name = name)


// изменения 18245 - она в MR

// изменения 18246 - из MR

// изменения 2 18246