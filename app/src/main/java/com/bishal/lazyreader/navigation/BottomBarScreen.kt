package com.bishal.lazyreader.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomBarScreen(
    val title: String,
    val route: String,
    val icon: ImageVector,

    ) {
    object Home: BottomBarScreen(
        title = "Home",
        route = ReaderScreen.ReaderHomeScreen.name,
        icon = Icons.Default.Home
    )
    object Search: BottomBarScreen(
        title = "Search",
        route = ReaderScreen.SearchScreen.name,
        icon = Icons.Filled.Search
    )
    object Profile: BottomBarScreen(
        title = "Profile",
        route = ReaderScreen.ReaderStatsScreen.name,
        icon = Icons.Filled.Person
    )
}
