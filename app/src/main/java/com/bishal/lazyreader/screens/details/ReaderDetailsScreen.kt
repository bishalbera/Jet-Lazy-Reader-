package com.bishal.lazyreader.screens.details

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.text.HtmlCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.bishal.lazyreader.components.ReaderAppBar
import com.bishal.lazyreader.data.Resource
import com.bishal.lazyreader.model.Item

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ReaderDetailsScreen(navController: NavController ,
                        bookId: String,
                        viewModel: ReaderDetailViewModel = hiltViewModel()){

    Scaffold(
        topBar = { ReaderAppBar(title = "Book Details",
            navController = navController,
            icon = Icons.Default.ArrowBack,
            showProfile = false,
            onBackArrowClicked = {navController.popBackStack()}) },
    ) {

        Surface(modifier = Modifier
            .padding(4.dp)
            .fillMaxSize()
            ) {
            Column(modifier = Modifier.padding(top = 15.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top) {

                val bookInfo = produceState<Resource<Item>>(initialValue = Resource.Loading()){
                    value = viewModel.getBookInfo(bookId)
                }.value
                if (bookInfo.data == null) {
                    Row() {
                        LinearProgressIndicator()
                        Text(text = "Loading...")
                    }

                }else{
                    ShowBookDetails(bookInfo, navController)
                }

            }

        }
    }


}

@Composable
fun ShowBookDetails(bookInfo: Resource<Item>,
                    navController: NavController) {
    val bookData = bookInfo.data?.volumeInfo
    val googleBookId = bookInfo.data?.id

    Card(modifier = Modifier.padding(34.dp),
        shape = CircleShape, elevation = CardDefaults.cardElevation(4.dp)
        ) {
        Image(painter = rememberAsyncImagePainter(model = bookData?.imageLinks?.thumbnail),
            contentDescription = "Book Image",
            modifier = Modifier
                .height(90.dp)
                .width(90.dp)
                .padding(1.dp))

    }
    Text(text = bookData?.title.toString(),
        style = MaterialTheme.typography.labelLarge,
        overflow = TextOverflow.Ellipsis,
        maxLines = 19)

    Text(text = "Authors: ${bookData?.authors.toString()}")
    Text(text = "Page Count: ${bookData?.pageCount.toString()}")
    Text(text = "Categories: ${bookData?.categories.toString()}",
        style = MaterialTheme.typography.labelSmall,
        maxLines = 3,
        overflow = TextOverflow.Ellipsis)
    Text(text = "Published: ${bookData?.publishedDate.toString()}",
        style = MaterialTheme.typography.labelSmall)
    Spacer(modifier = Modifier.height(5.dp))

    val cleanDescription = HtmlCompat.fromHtml(
        bookData?.description.toString(),
    HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
    val localDims = LocalContext.current.resources.displayMetrics
    Surface(modifier = Modifier
        .height(localDims.heightPixels.dp.times(0.09f))
        .padding(4.dp),
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp, Color.DarkGray)) {

        LazyColumn(modifier = Modifier.padding(4.dp)) {
            item {
                Text(text = cleanDescription)
            }
        }

    }
}
