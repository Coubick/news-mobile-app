package com.example.news.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.example.news.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

}