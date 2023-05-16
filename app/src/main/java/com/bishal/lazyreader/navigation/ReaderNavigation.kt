@file:OptIn(ExperimentalComposeUiApi::class, ExperimentalComposeUiApi::class,
    ExperimentalAnimationApi::class
)

package com.bishal.lazyreader.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.bishal.lazyreader.screens.details.ReaderDetailsScreen
import com.bishal.lazyreader.screens.home.HomeScreenViewModel
import com.bishal.lazyreader.screens.home.ReaderHomeScreen
import com.bishal.lazyreader.screens.login.ReaderLoginScreen
import com.bishal.lazyreader.screens.lottie.ReaderLottieScreen
import com.bishal.lazyreader.screens.search.ReaderSearchScreen
import com.bishal.lazyreader.screens.stats.ReaderStatsScreen
import com.bishal.lazyreader.screens.update.BookUpdateScreen
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ReaderNavigation() {
    val navController = rememberAnimatedNavController()

    AnimatedNavHost(
        navController = navController,
        startDestination = ReaderScreen.LottieScreen.name,
        enterTransition = {
            slideInHorizontally(
                initialOffsetX = {1000},
                animationSpec = tween(300, easing = FastOutSlowInEasing)
            ) + fadeIn(animationSpec = tween(300))

        },
        exitTransition = {
            slideOutHorizontally(
                targetOffsetX = {-1000},
                animationSpec = tween(300, easing = FastOutSlowInEasing)
            ) + fadeOut(animationSpec = tween(300))
        },
        popEnterTransition = {
            slideInHorizontally(
                initialOffsetX = {1000},
                animationSpec = tween(300, easing = FastOutSlowInEasing)
            ) + fadeIn(animationSpec = tween(300))

        },
        popExitTransition = {
            slideOutHorizontally(
                targetOffsetX = {-1000},
                animationSpec = tween(300, easing = FastOutSlowInEasing)
            ) + fadeOut(animationSpec = tween(300))
        }

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
