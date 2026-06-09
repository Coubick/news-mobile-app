package com.example.news.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.news.presentation.screens.auth.LoginScreen
import com.example.news.presentation.screens.auth.RegistrationScreen
import com.example.news.presentation.viewmodel.LoginViewModel
import com.example.news.presentation.viewmodel.RegistrationViewModel
import kotlinx.serialization.Serializable

sealed class Screen{
    @Serializable
    data object Login : Screen()

    @Serializable
    data object Registration : Screen()

    @Serializable
    data object Main : Screen()

    @Serializable
    data object Profile : Screen()

    @Serializable
    data object NewAddForm : Screen()
}

@Composable
fun AppNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Screen.Login
    ) {
        composable<Screen.Login> {
            val viewModel: LoginViewModel = hiltViewModel()
            LoginScreen(
                viewModel = viewModel,
                onNavigateTo = { navController.navigate(it) }
            )
        }

        composable<Screen.Registration> {
            val viewModel: RegistrationViewModel = hiltViewModel()
            RegistrationScreen(
                viewModel = viewModel,
                onNavigateTo = { navController.navigate(it) }
            )
        }
    }
}