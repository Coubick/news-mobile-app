package com.example.news.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.news.data.dto.toNewsItem
import com.example.news.domain.repository.NewsRepository
import com.example.news.domain.model.NewsItem
import com.example.news.utils.Constants.Companion.LIMIT_ITEMS
import com.example.news.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class NewsFilter(
    val cityId: Long? = null,
    val sphereId: Long? = null
)

data class NewsListUiState(
    val newsList: List<NewsItem> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isRefreshing: Boolean = false,
    val currentFilter: NewsFilter = NewsFilter(),
    val hasMore: Boolean = true,
    val currentPage: Int = 0
)

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val newsRepository: NewsRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(NewsListUiState())
    val uiState: StateFlow<NewsListUiState> = _uiState.asStateFlow()

    init {
        loadNews()
    }

    fun loadNews() {
        val currentFilter = _uiState.value.currentFilter
        viewModelScope.launch {
            val result = newsRepository.getNewsList(
                cityId = currentFilter.cityId,
                sphereId = currentFilter.sphereId,
                limit = LIMIT_ITEMS,
                offset = 0
            )

            when (result) {
                is Result.Success -> {
                    val newsItems = result.data!!.map { it.toNewsItem() }
                    _uiState.update {
                        it.copy(
                            isLoading = true,
                            isRefreshing = false,
                            errorMessage = null,
                            newsList = newsItems,
                            hasMore = newsItems.size >= 20,
                            currentPage = 1
                        )
                    }
                }

                is Result.Failure -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isRefreshing = false,
                            errorMessage = result.msg
                        )
                    }
                }
            }
        }
    }

    fun applyFiltersAndLoad(cityId: Long?, sphereId: Long?) {
        viewModelScope.launch {
            _uiState.update {
                val filter = NewsFilter(cityId, sphereId)
                it.copy(currentFilter = filter)
            }
            loadNews()
        }
    }

    fun loadMore() {
        if (!_uiState.value.isLoading || !_uiState.value.hasMore) return

        viewModelScope.launch {
            val filter = _uiState.value.currentFilter
            val nextPage = _uiState.value.currentPage

            val result = newsRepository.getNewsList(
                cityId = filter.cityId,
                sphereId = filter.sphereId,
                limit = LIMIT_ITEMS,
                offset = nextPage * 20
            )

            when (result) {
                is Result.Success -> {
                    val newsItems = result.data!!.map { it.toNewsItem() }
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isRefreshing = false,
                            errorMessage = null,
                            hasMore = newsItems.size >= 20,
                            currentPage = nextPage + 1,
                            newsList = it.newsList + newsItems
                        )
                    }
                }

                is Result.Failure -> {
                    _uiState.update { it.copy(isLoading = false) }
                }
            }
        }
    }
}