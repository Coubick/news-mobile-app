package com.example.news.utils

import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem(
    val iconSelected: ImageVector,
    val iconUnselected: ImageVector,
    val titleResId: Int,
    val route: Any
)