package com.bishal.lazyreader.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.bishal.lazyreader.screens.home.ReaderHomeScreen
import com.bishal.lazyreader.screens.login.ReaderLoginScreen
import com.bishal.lazyreader.screens.splash.ReaderSplashScreen

@Composable
fun ReaderNavigation(){
val navController = rememberNavController()
    NavHost(navController = navController,
    startDestination = ReaderScreen.SplashScreen.name){
        composable(ReaderScreen.SplashScreen.name){
            ReaderSplashScreen(navController = navController)
        }
        composable(ReaderScreen.LoginScreen.name){
            ReaderLoginScreen(navController = navController)
        }
        composable(ReaderScreen.ReaderHomeScreen.name){
            ReaderHomeScreen(navController = navController)
        }
    }
}