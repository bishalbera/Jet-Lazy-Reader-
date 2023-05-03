@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class
)

package com.bishal.lazyreader.screens.home

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.bishal.lazyreader.components.FABContent
import com.bishal.lazyreader.components.ReaderAppBar

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ReaderHomeScreen(navController: NavController){
    

    Scaffold(topBar = {
                      ReaderAppBar(title = "Lazy Reader", navController = navController)
    },
        floatingActionButton = {
            FABContent{
                
            }

    }) {
        Surface(modifier = Modifier.fillMaxSize()) {

        }


    }
}


