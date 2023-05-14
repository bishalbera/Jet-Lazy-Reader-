@file:OptIn(ExperimentalComposeUiApi::class, ExperimentalComposeUiApi::class)

package com.bishal.lazyreader.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.bishal.lazyreader.screens.details.ReaderDetailsScreen
import com.bishal.lazyreader.screens.home.HomeScreenViewModel
import com.bishal.lazyreader.screens.home.ReaderHomeScreen
import com.bishal.lazyreader.screens.login.ReaderLoginScreen
import com.bishal.lazyreader.screens.lottie.ReaderLottieScreen
import com.bishal.lazyreader.screens.search.ReaderSearchScreen
import com.bishal.lazyreader.screens.search.ReaderSearchScreenViewModel
import com.bishal.lazyreader.screens.stats.ReaderStatsScreen
import com.bishal.lazyreader.screens.update.BookUpdateScreen

@Composable
fun ReaderNavigation(){
val navController = rememberNavController()
    NavHost(navController = navController,
    startDestination = ReaderScreen.LottieScreen.name){
        composable(ReaderScreen.LottieScreen.name){
            ReaderLottieScreen(navController = navController)
        }
        composable(ReaderScreen.LoginScreen.name){
            ReaderLoginScreen(navController = navController)
        }
        composable(ReaderScreen.ReaderHomeScreen.name){
            val homeViewModel = hiltViewModel<HomeScreenViewModel>()
            ReaderHomeScreen(navController = navController, viewModel = homeViewModel)
        }
        composable(ReaderScreen.ReaderStatsScreen.name){
            ReaderStatsScreen(navController = navController)
        }
        composable(ReaderScreen.SearchScreen.name){
            val searchViewModel = hiltViewModel<ReaderSearchScreenViewModel>()
            ReaderSearchScreen(navController = navController, viewModel = searchViewModel)
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