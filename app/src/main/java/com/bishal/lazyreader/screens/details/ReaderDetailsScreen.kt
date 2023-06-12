package com.bishal.lazyreader.screens.details

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.bishal.lazyreader.data.Resource
import com.bishal.lazyreader.model.Item
import com.bishal.lazyreader.utils.PaletteGenerator.convertImageUrlToBitmap
import com.bishal.lazyreader.utils.PaletteGenerator.extractColorsFromBitmap

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")

@Composable
fun ReaderDetailsScreen(
    navController: NavController ,
    viewModel: ReaderDetailViewModel = hiltViewModel(),
    bookId: String
) {
    val colorPalette by viewModel.colorPalette
//    val bookInfo = produceState<Resource<Item>>(initialValue = Resource.Loading()){
//        value = viewModel.getBookInfo(bookId)
//    }.value

//    DetailsContent(
//        navController = navController,
//        bookId,
//        colors = colorPalette
//
//    )



//    if (colorPalette.isNotEmpty()) {
//
//        DetailsContent(
//            navController = navController,
//            bookId = bookId,
//            colors = colorPalette
//        )
//
//    } else {
//        viewModel.generateColorPalette()
//    }
//
//    val context = LocalContext.current
//
//    LaunchedEffect(key1 = true, ) {
//        viewModel.uiEvent.collectLatest { event ->
//            when (event) {
//                is UiEvent.GenerateColorPalette -> {
//                    val bitmap = bookInfo.data?.volumeInfo?.imageLinks?.let {
//                        convertImageUrlToBitmap(
//                            imageUrl = it.thumbnail,
//                            context = context
//                        )
//
//                    }
//                    if (bitmap != null) {
//                        viewModel.setColorPalette(
//                            colors = extractColorsFromBitmap(
//                                bitmap = bitmap
//                            )
//                        )
//                    }
//                }
//            }
//
//        }
//
//    }



    val bookInfo = produceState<Resource<Item>>(initialValue = Resource.Loading()) {
        value = viewModel.getBookInfo(bookId)
    }.value
    var launchedEffectTriggered by remember { mutableStateOf(false) }

    if (bookInfo is Resource.Success) {
        viewModel.onBookInfoLoaded(bookInfo)
    }


    val imageUrl = bookInfo?.data?.volumeInfo?.imageLinks?.thumbnail


    val context = LocalContext.current
    LaunchedEffect(key1 = true, ) {
        try {
            val bitmap = convertImageUrlToBitmap(
                imageUrl = imageUrl.toString(),
                context = context
            )

            if (bitmap != null) {
                launchedEffectTriggered = true
                viewModel.setColorPalette(
                    colors = extractColorsFromBitmap(
                        bitmap = bitmap
                    )
                )
            }
        }catch (e: Exception) {
            Log.d("ex", e.message.toString())
        }

    }


        DetailsContent(
            navController = navController,
            colors = colorPalette,
            bookId = bookId,
            bookInfo = bookInfo
        )

}

