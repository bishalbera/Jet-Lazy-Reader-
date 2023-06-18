@file:OptIn(ExperimentalComposeUiApi::class, ExperimentalComposeUiApi::class,
    ExperimentalAnimationApi::class
)

package com.bishal.lazyreader.navigation


//import com.bishal.lazyreader.screens.lottie.ReaderLottieScreen
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.bishal.lazyreader.screens.details.ReaderDetailsScreen
import com.bishal.lazyreader.screens.home.HomeScreenViewModel
import com.bishal.lazyreader.screens.home.ReaderHomeScreen
import com.bishal.lazyreader.screens.login.ReaderLoginScreen
import com.bishal.lazyreader.screens.lottie.ReaderLottieScreen
import com.bishal.lazyreader.screens.search.ReaderSearchScreen
import com.bishal.lazyreader.screens.stats.ReaderStatsScreen
import com.bishal.lazyreader.screens.update.BookUpdateScreen
import com.bishal.lazyreader.ui.theme.Purple500


@OptIn(ExperimentalComposeUiApi::class, ExperimentalAnimationApi::class)
@Composable
fun ReaderNavigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = ReaderScreen.LottieScreen.name,


        ) {
        composable(ReaderScreen.LottieScreen.name) {
            ReaderLottieScreen(navController = navController)
        }
        composable(ReaderScreen.LoginScreen.name) {
            ReaderLoginScreen(navController = navController)
        }
        composable(ReaderScreen.ReaderHomeScreen.name) {
            val homeViewModel = hiltViewModel<HomeScreenViewModel>()
            ReaderHomeScreen(navController = navController, viewModel = homeViewModel)
        }
        composable(ReaderScreen.ReaderStatsScreen.name) {
            val homeViewModel = hiltViewModel<HomeScreenViewModel>()
            ReaderStatsScreen(navController = navController, viewModel = homeViewModel)
        }
        composable(ReaderScreen.SearchScreen.name) {
            ReaderSearchScreen(navController = navController)
        }

        val detailName = ReaderScreen.DetailScreen.name
        composable("$detailName/{bookId}", arguments = listOf(navArgument("bookId"){
            type = NavType.StringType
        })) {backStackEntry ->
            backStackEntry.arguments?.getString("bookId").let {
                ReaderDetailsScreen(navController = navController, bookId = it.toString())
            }

        }

        val updateName = ReaderScreen.UpdateScreen.name
        composable("$updateName/{bookItemId}",
            arguments = listOf(navArgument("bookItemId") {
                type = NavType.StringType
            })) { navBackStackEntry ->

            navBackStackEntry.arguments?.getString("bookItemId").let {
                BookUpdateScreen(navController = navController, bookItemId = it.toString())
            }

        }
    }

}

@Composable
fun BottomBar(
    navController: NavController,
    modifier: Modifier = Modifier,
    onItemClick: (BottomBarScreen) -> Unit
) {
    val screens = listOf(
        BottomBarScreen.Home,
        BottomBarScreen.Search,
        BottomBarScreen.Profile
    )
    val backStackEntry = navController.currentBackStackEntryAsState()

    BottomNavigation(
        modifier = modifier.clip(RoundedCornerShape(13.dp))
            .height(35.dp),
        backgroundColor = Color.DarkGray,
        elevation = 5.dp
    ) {
        screens.forEach { screen ->
            val selected = screen.route == backStackEntry.value?.destination?.route
            val background = if (selected) Purple500.copy(alpha = 0.6f) else Color.Transparent
            val contentColor = if (selected) Color.White else Color.Black
            BottomNavigationItem(
                selected = selected,
                onClick = { onItemClick(screen) },
                selectedContentColor = Color.Green,
                unselectedContentColor = Color.Gray,
                icon = {
                    Box( modifier = Modifier
                        .height(40.dp)
                        .clip(CircleShape)
                        .background(background)
                    ) {
                        Row(modifier = Modifier
                            .padding(
                                start = 10.dp,
                                end = 10.dp,
                                top = 8.dp,
                                bottom = 8.dp
                            ),
                            verticalAlignment = CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = screen.icon,
                                contentDescription = "icon",
                                tint = contentColor
                            )
                            AnimatedVisibility(visible = selected) {
                                Text(
                                    text = screen.title,
                                    color = contentColor
                                )
                            }

                        }

                    }

                }
            )
        }
    }
}




