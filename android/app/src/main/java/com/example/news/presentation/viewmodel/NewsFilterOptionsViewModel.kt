package com.example.news.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.news.data.repository.NewsFilterOptionsRepository
import com.example.news.presentation.model.FilterItem

import com.example.news.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FilterUiState(
    val cities: List<FilterItem>? = emptyList(),
    val spheres: List<FilterItem>? = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class NewsFilterOptionsViewModel @Inject constructor(
    private val newsFilterOptionsRepository: NewsFilterOptionsRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(value = FilterUiState())
    val uiState: StateFlow<FilterUiState> = _uiState.asStateFlow()

    init {
        loadSpheresAndCities()
    }

    fun loadSpheresAndCities() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val citiesResult = async {
                newsFilterOptionsRepository.getAllCities()
            }

            val spheresResult = async {
                newsFilterOptionsRepository.getAllSpheres()
            }

            val cities = citiesResult.await()
            val spheres = spheresResult.await()

            var errorMessage: String? = null
            when (cities) {
                is Result.Success ->
                    _uiState.update {
                        val citiesCasted = cities.data
                        it.copy(cities = citiesCasted)
                    }

                is Result.Failure ->
                    errorMessage = cities.msg
            }

            when (spheres) {
                is Result.Success ->
                    _uiState.update {
                        val spheresCasted = spheres.data
                        it.copy(spheres = spheresCasted)
                    }

                is Result.Failure ->
                    errorMessage = spheres.msg
            }

            _uiState.update {
                it.copy(isLoading = false, errorMessage = errorMessage)
            }
        }
    }
}