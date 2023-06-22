@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class
)

package com.bishal.lazyreader.presentation.screens.home

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.bishal.lazyreader.domain.model.MBook
import com.bishal.lazyreader.navigation.BottomBar
import com.bishal.lazyreader.navigation.ReaderScreen
import com.bishal.lazyreader.presentation.common.ListCard
import com.bishal.lazyreader.presentation.common.LoadingAnimation
import com.bishal.lazyreader.presentation.common.ReaderAppBar
import com.bishal.lazyreader.presentation.common.TitleSection
import com.google.firebase.auth.FirebaseAuth

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ReaderHomeScreen(navController: NavController,
                     viewModel: HomeScreenViewModel = hiltViewModel()){


    Scaffold(
        topBar = {
            ReaderAppBar(title = "Lazy Reader", navController = navController)
        },

        bottomBar = {

            BottomBar(
                navController = navController,
                onItemClick = {
                    navController.navigate(it.route)
                }
            )
        }) {
        Surface(modifier = Modifier.fillMaxSize()) {
            //home content
            HomeContent(navController, viewModel)

        }


    }





    
}

@Composable
fun HomeContent(navController: NavController,
                viewModel: HomeScreenViewModel
) {
    var listOfBooks = listOf<MBook>()
    val currentUser = FirebaseAuth.getInstance().currentUser

    if (!viewModel.data.value.data.isNullOrEmpty()) {
        listOfBooks = viewModel.data.value.data!!.toList().filter { mBook ->
        mBook.userId == currentUser?.uid.toString()

        }
        Log.d("Books", "Homecontent: $listOfBooks")
    }




    val displayName = FirebaseAuth.getInstance().currentUser?.displayName
    val currentUserName = if (!displayName.isNullOrEmpty())
        displayName

    else FirebaseAuth.getInstance().currentUser?.email?.split("@")?.get(0)
    Column(
        Modifier.padding(top = 65.dp),
        verticalArrangement = Arrangement.Top

    ) {
        Row(modifier = Modifier.align(alignment = Alignment.Start)) {
            TitleSection(label = "Currently Reading")
            Spacer(modifier = Modifier.fillMaxWidth(0.7f))

            Column {
                if (FirebaseAuth.getInstance().currentUser?.photoUrl != null){
                    AsyncImage(model = currentUser?.photoUrl,
                        contentDescription = "Profile picture",
                        modifier = Modifier
                            .clickable {
                                navController.navigate(ReaderScreen.ReaderStatsScreen.name)
                            }
                            .size(45.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop)
                }else{
                    Icon(
                        imageVector = Icons.Filled.AccountCircle,
                        contentDescription = "profile",
                        modifier = Modifier
                            .size(45.dp)
                            .clickable {
                                navController.navigate(ReaderScreen.ReaderStatsScreen.name)
                            }
                    )
                }

                if (currentUserName != null) {
                    Text(
                        text = currentUserName,
                        modifier = Modifier.padding(2.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.Red,
                        fontSize = 15.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Clip)
                }
                Divider()


            }


        }
        ReadingRightNowArea(listOfBooks = listOfBooks,
            navController = navController)

        TitleSection(label = "Reading List")

        BookListArea(listOfBooks = listOfBooks,
            navController = navController)


       


    }

}

@Composable
fun BookListArea(listOfBooks: List<MBook>,
                 navController: NavController) {
    val addedBooks = listOfBooks.filter { mBook ->
    mBook.startedReading == null && mBook.finishedReading == null

    }
    HorizontalScrollableComponent(addedBooks){
        Log.d("TAG", "BookListArea: $it")
        navController.navigate(ReaderScreen.UpdateScreen.name + "/$it")
    }



}

@Composable
fun HorizontalScrollableComponent(listOfBooks: List<MBook>,
                                  viewModel: HomeScreenViewModel = hiltViewModel(),
                                  onCardPressed: (String) -> Unit) {
    val scrollState = rememberScrollState()
    
    Row(modifier = Modifier
        .fillMaxWidth()
        .heightIn(280.dp)
        .horizontalScroll(scrollState)) {
        if (viewModel.data.value.loading == true) {
            LoadingAnimation()
        }else{
            if (listOfBooks.isNullOrEmpty()) {
                Surface(modifier = Modifier.padding(23.dp)) {
                    Text(text = "No books found 😕, Add a book 🙃",
                        style = TextStyle(color = Color.Red.copy(alpha = 0.4f),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                        )
                    )
                }
            }else{
                for (book in listOfBooks) {
                    ListCard(book ) {
                        onCardPressed(book.googleBookId.toString())

                    }
                }
            }
        }



    }
}


@Composable
fun ReadingRightNowArea(listOfBooks: List<MBook>,
                        navController: NavController) {
    //Filter books by reading now
    val readingNowList = listOfBooks.filter { mBook ->
        mBook.startedReading != null && mBook.finishedReading == null
    }

    HorizontalScrollableComponent(readingNowList){
        Log.d("TAG", "BoolListArea: $it")
        navController.navigate(ReaderScreen.UpdateScreen.name + "/$it")
    }

}