package com.example.news.presentation.screens.news

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.news.presentation.navigation.BottomNavigationBar
import com.example.news.presentation.navigation.NewsScreenNavigationRoute
import com.example.news.presentation.screens.profile.ProfileScreen

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = NewsScreenNavigationRoute.Feed,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable<NewsScreenNavigationRoute.Feed> {
                NewsListScreen()
            }
            composable<NewsScreenNavigationRoute.AddNew> {
                CreateNewsScreen()
            }
            composable<NewsScreenNavigationRoute.Profile> {
                ProfileScreen()
            }
        }
    }
}