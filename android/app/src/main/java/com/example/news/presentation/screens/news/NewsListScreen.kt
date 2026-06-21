package com.example.news.presentation.screens.news

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.news.R
import com.example.news.presentation.components.NewsCard
import com.example.news.presentation.model.FilterItem
import com.example.news.presentation.viewmodel.NewsFilter
import com.example.news.presentation.viewmodel.NewsFilterOptionsViewModel
import com.example.news.presentation.viewmodel.NewsViewModel
import com.example.news.utils.FilterOptionsType


@Composable
private fun NewsListHeader(
    currentFilter: NewsFilter,
    cities: List<FilterItem>?,
    spheres: List<FilterItem>?,
    onCitySelected: (Long?) -> Unit,
    onSphereSelected: (Long?) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        FilterDropdown(
            selectedOptionId = (currentFilter.cityId),
            options = cities,
            onOptionSelected = onCitySelected,
            modifier = Modifier.weight(1f),
            optionsType = FilterOptionsType.CITY
        )

        VerticalDivider(
            modifier = Modifier
                .height(30.dp)
                .padding(horizontal = 8.dp),
            color = Color.Gray
        )

        FilterDropdown(
            selectedOptionId = (currentFilter.sphereId),
            options = spheres,
            onOptionSelected = onSphereSelected,
            modifier = Modifier.weight(2f),
            optionsType = FilterOptionsType.SPHERE
        )

        Spacer(modifier = Modifier.width(8.dp))

        Image(
            painter = painterResource(id = R.drawable.default_avatar),
            contentDescription = "Аватар",
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
fun NewsListContent(
    newsList: List<NewsItem> = emptyList(),
    isLoading: Boolean = false,
    currentFilter: NewsFilter,
    cities: List<FilterItem>?,
    spheres: List<FilterItem>?,
    onCitySelected: (Long?) -> Unit = {},
    onSphereSelected: (Long?) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        NewsListHeader(
            currentFilter,
            cities,
            spheres,
            onCitySelected,
            onSphereSelected
        )

        HorizontalDivider(
            color = Color.Gray,
            modifier = Modifier
        )

        if (!newsList.isEmpty()) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(top = 8.dp, bottom = 80.dp)
            ) {
                items(
                    items = newsList,
                    key = { it.id }
                ) { news ->
                    NewsCard(
                        news = news
                    )
                }
            }
        } else {
            Text(
                text = "Новостей пока нет...",
                textAlign = TextAlign.Center
            )
        }
    }

}

@Composable
fun NewsListScreen(
    viewModel: NewsViewModel = hiltViewModel(),
    viewModelFilter: NewsFilterOptionsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val uiStateFilter by viewModelFilter.uiState.collectAsStateWithLifecycle()

    NewsListContent(
        newsList = uiState.newsList,
        isLoading = uiState.isLoading,
        currentFilter = uiState.currentFilter,
        cities = uiStateFilter.cities,
        spheres = uiStateFilter.spheres,
        onCitySelected = { cityId ->
            viewModel.applyFiltersAndLoad(
                cityId = cityId,
                sphereId = uiState.currentFilter.sphereId
            )
        },
        onSphereSelected = { sphereId ->
            viewModel.applyFiltersAndLoad(
                cityId = uiState.currentFilter.cityId,
                sphereId = sphereId
            )
        },
    )
}


//@Preview(showBackground = true, name = "Пустой список")
//@Composable
//fun NewsListPreviewEmpty() {
//    NewsListContent(
//        newsList = emptyList(),
//        isLoading = false,
//        currentFilter = NewsFilter(),  // ← Пустой фильтр
//        cities = listOf(
//            FilterItem(id = 1, name = "Москва"),
//            FilterItem(id = 2, name = "Санкт-Петербург"),
//            FilterItem(id = 3, name = "Новосибирск")
//        ),
//        spheres = listOf(
//            FilterItem(id = 1, name = "Спорт"),
//            FilterItem(id = 2, name = "Культура"),
//            FilterItem(id = 3, name = "Технологии")
//        ),
//        onCitySelected = {},
//        onSphereSelected = {}
//    )
//}

//@Preview(showBackground = true, name = "С новостями")
//@Composable
//fun NewsListPreviewWithData() {
//    NewsListContent(
//        newsList = listOf(
//            NewsItem(
//                id = 1,
//                title = "Тестовая новость",
//                description = "Описание новости",
//                imageUrl = null,
//                authorName = "Иван Петров",
//                authorAvatarUrl = null,
//                cityName = "Москва",
//                sphereName = "Спорт",
//                createdAt = "2 часа назад"
//            ),
//            NewsItem(
//                id = 2,
//                title = "Тестовая новость 2",
//                description = "Описание новости 2 2 2 22 22 2",
//                imageUrl = null,
//                authorName = "Иван Матвеев",
//                authorAvatarUrl = null,
//                cityName = "Мурманск",
//                sphereName = "Культура",
//                createdAt = "когда-то"
//            )
//        ),
//        isLoading = false,
//        currentFilter = NewsFilter(cityId = 1L, sphereId = 2L),  // ← Выбраны фильтры
//        cities = listOf(
//            FilterItem(id = 1, name = "Москва"),
//            FilterItem(id = 2, name = "Санкт-Петербург")
//        ),
//        spheres = listOf(
//            FilterItem(id = 1, name = "Спорт"),
//            FilterItem(id = 2, name = "Культура")
//        ),
//        onCitySelected = {},
//        onSphereSelected = {}
//    )
//}

//@Preview(showBackground = true, name = "Загрузка")
//@Composable
//fun NewsListPreviewLoading() {
//    NewsListContent(
//        newsList = emptyList(),
//        isLoading = true,
//        currentFilter = NewsFilter(),
//        cities = emptyList(),
//        spheres = emptyList(),
//        onCitySelected = {},
//        onSphereSelected = {}
//    )
//}
//
//@Preview(showBackground = true, name = "С выбранным городом")
//@Composable
//fun NewsListPreviewWithCityFilter() {
//    NewsListContent(
//        newsList = emptyList(),
//        isLoading = false,
//        currentFilter = NewsFilter(cityId = 2L),
//        cities = listOf(
//            FilterItem(id = 1, name = "Москва"),
//            FilterItem(id = 2, name = "Санкт-Петербург"),
//            FilterItem(id = 3, name = "Новосибирск")
//        ),
//        spheres = listOf(
//            FilterItem(id = 1, name = "Спорт"),
//            FilterItem(id = 2, name = "Культура")
//        ),
//        onCitySelected = {},
//        onSphereSelected = {}
//    )
//}