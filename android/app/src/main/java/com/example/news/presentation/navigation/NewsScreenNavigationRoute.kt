package com.example.news.presentation.navigation

import kotlinx.serialization.Serializable

sealed interface NewsScreenNavigationRoute {
    @Serializable
    data object Feed: NewsScreenNavigationRoute
    @Serializable
    data object Profile : NewsScreenNavigationRoute
    @Serializable
    data object AddNew: NewsScreenNavigationRoute
}