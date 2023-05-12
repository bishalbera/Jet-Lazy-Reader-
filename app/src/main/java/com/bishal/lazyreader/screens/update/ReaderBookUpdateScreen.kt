@file:OptIn(ExperimentalMaterial3Api::class)

package com.bishal.lazyreader.screens.update

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.bishal.lazyreader.components.ReaderAppBar
import com.bishal.lazyreader.data.DataOrException
import com.bishal.lazyreader.model.MBook
import com.bishal.lazyreader.screens.home.HomeScreenViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun BookUpdateScreen(
    navController: NavController,
    bookItemId: String,
     viewModel: HomeScreenViewModel = hiltViewModel()){
    Scaffold(topBar = { ReaderAppBar(title = "Update Book",
        icon = Icons.Default.ArrowBack,
        showProfile = false,
        navController = navController) {
        navController.popBackStack()
    }}) {
        val bookInfo = produceState<DataOrException<List<MBook>,
                Boolean,
                Exception>>(initialValue = DataOrException(data = emptyList(),
            true, Exception(""))
        ){
            value = viewModel.data.value
        }.value

        Surface(modifier = Modifier
            .fillMaxSize()
            .padding(3.dp)) {
            Column(
                modifier = Modifier.padding(top = 3.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = CenterHorizontally
            ) {
                Log.d("INFO", "BookUpdateScreen: ${viewModel.data.value.data.toString()}")
                if (bookInfo.loading == true) {
                    LinearProgressIndicator()
                    bookInfo.loading = false

                }else {
                    Text(text = viewModel.data.value.data?.get(0)?.title.toString())
                }
            }



    }

}
}