package com.example.news.presentation.screens.news

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.AddCircleOutline
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.news.R
import com.example.news.presentation.navigation.NewsScreenNavigationRoute
import com.example.news.utils.BottomNavItem

@Composable
fun BottomNavigationBar(
    navController: NavController
) {
    val navigationItems = listOf(
        BottomNavItem(
            iconSelected = Icons.Filled.Home,
            iconUnselected = Icons.Outlined.Home,
            titleResId = R.string.feed,
            route = NewsScreenNavigationRoute.Feed
        ),
        BottomNavItem(
            iconSelected = Icons.Filled.AddCircle,
            iconUnselected = Icons.Outlined.AddCircleOutline,
            titleResId = R.string.add,
            route = NewsScreenNavigationRoute.AddNew
        ),
        BottomNavItem(
            iconSelected = Icons.Filled.Person,
            iconUnselected = Icons.Outlined.Person,
            titleResId = R.string.profile,
            route = NewsScreenNavigationRoute.Profile
        )
    )

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination

    NavigationBar {
        navigationItems.forEach { item ->
            val selected = currentDestination?.hierarchy?.any {
                it.hasRoute(item.route::class)
            } == true

            NavigationBarItem(
                selected = selected,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    val icon = if (selected) item.iconSelected else item.iconUnselected
                    Icon(
                        imageVector = icon,
                        contentDescription = stringResource(item.titleResId)
                    )
                },
                label = { Text(stringResource(item.titleResId)) }
            )
        }
    }
}