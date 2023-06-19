package com.bishal.lazyreader.screens.stats

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.sharp.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.bishal.lazyreader.components.ReaderAppBar
import com.bishal.lazyreader.model.MBook
import com.bishal.lazyreader.navigation.BottomBar
import com.bishal.lazyreader.screens.home.HomeScreenViewModel
import com.bishal.lazyreader.screens.search.shimmerEffect
import com.bishal.lazyreader.utils.formatDate
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import java.util.Locale


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReaderStatsScreen(navController: NavController,
                      viewModel: HomeScreenViewModel = hiltViewModel()){

    var books: List<MBook>
    val currentUser = FirebaseAuth.getInstance().currentUser

    Scaffold(
        topBar = {
            ReaderAppBar(
                title = "Book Stats",
                icon = Icons.Default.ArrowBack,
                showProfile = false,
                navController = navController,
            ) {
                navController.popBackStack()
            }
        },
        bottomBar = {

             BottomBar(
                 navController = navController,
                 onItemClick = {
                     navController.navigate(it.route)
                 }
             )


        }
    ) {
        Surface(modifier = Modifier.padding(top = 80.dp)) {
            // only shows the books read by this user
            books = if (!viewModel.data.value.data.isNullOrEmpty()) {
                viewModel.data.value.data!!.filter { mBook ->
                    (mBook.userId == currentUser?.uid)

                }
            }else{
                emptyList()
            }
            Column {
                Row{
                  Box(modifier = Modifier
                      .size(45.dp)
                      .padding(2.dp)){
                      Icon(imageVector = Icons.Sharp.Person,
                          contentDescription = "person icon")

                  }
                    Text(
                        text = "Hello, ${  
                            currentUser?.email.toString().split("@")[0].uppercase(
                                Locale.getDefault()
                            )
                        }"
                    )
                }

                Card(modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                    shape = CircleShape,

                ) {
                    val readBooksList: List<MBook> = if (!viewModel.data.value.data.isNullOrEmpty()) {
                        books.filter { mBook ->
                            (mBook.userId == currentUser?.uid) && (mBook.finishedReading != null)
                        }

                    }else {
                        emptyList()
                    }

                    val readingBooks = books.filter { mBook ->
                        (mBook.startedReading != null && mBook.finishedReading == null)
                    }

                    Column(modifier = Modifier.padding(start = 25.dp, top = 4.dp, bottom = 4.dp),
                        horizontalAlignment = Alignment.Start) {
                        Text(text = "Your Stats", style = MaterialTheme.typography.headlineMedium)
                        Divider()
                        Text(text = "You're reading: ${readingBooks.size} books")
                        Text(text = "You've read: ${readBooksList.size} books")

                    }

                }

                if (viewModel.data.value.loading == true) {
                    LinearProgressIndicator()
                }else {
                    Divider()
                    var isLoading by remember { mutableStateOf(true) }
                    
                    LaunchedEffect(key1 = true){
                        delay(3000)
                        isLoading = false
                    }
                    LazyVerticalGrid(modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                        //contentPadding = PaddingValues(16.dp),
                        columns = GridCells.Fixed(2)
                    ){
                        //filter books by finished ones
                        val readBooks: List<MBook> = if (!viewModel.data.value.data.isNullOrEmpty()){
                            viewModel.data.value.data!!.filter { mBook ->
                                (mBook.userId == currentUser?.uid) && (mBook.finishedReading != null)
                            }
                        }else {
                            emptyList()

                        }
                        items(items = readBooks) {book ->
                            BookRowStats(
                                book = book,
                                isLoading = isLoading,
                                contentAfterLoading = {
                                    ContentAfterLoading(book = book)
                                }
                            )
                        }

                    }
                }

            }





        }

    }

}




@Composable
fun BookRowStats(
    book: MBook,
    isLoading: Boolean,
    contentAfterLoading: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    if (isLoading){
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(3.dp)
                .shimmerEffect()
                .clip(RoundedCornerShape(13.dp))
        ) {
            Row(
                modifier = Modifier
                    .shimmerEffect()
                    .padding(5.dp),
                verticalAlignment = Alignment.Top
            ) {
                Box(
                    modifier = Modifier
                        .width(80.dp)
                        .fillMaxHeight()
                        .padding(end = 4.dp)
                        .shimmerEffect()
                )
                Column() {

                }

            }

        }


    } else{
        contentAfterLoading()
    }

}

@Composable
fun ContentAfterLoading(book: MBook) {

    val context = LocalContext.current
    val resources = context.resources

    val displayMetrics = resources.displayMetrics

    val screenWidth = displayMetrics.widthPixels / displayMetrics.density
    val spacing = 10.dp

    Card(modifier = Modifier
        .clickable {}
        .width(242.dp)
        .height(236.dp)
        .padding(6.dp),
        shape = RoundedCornerShape(29.dp),
        elevation = CardDefaults.cardElevation(16.dp)
    ) {
        Column(
            modifier = Modifier.width(screenWidth.dp - (spacing * 2)),
            horizontalAlignment = Alignment.Start
        ) {
            Row(horizontalArrangement = Arrangement.Center) {
                Image(
                    painter = rememberAsyncImagePainter(model = book.photoUrl.toString()),
                    contentDescription = "book image",
                    modifier = Modifier
                        .height(120.dp)
                        .width(80.dp)
                        .padding(4.dp)
                )
                Spacer(modifier = Modifier.width(50.dp))

                Column(
                    modifier = Modifier.padding(top = 25.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (book.rating!! >= 4 ) {
                        Icon(
                            imageVector = Icons.Default.ThumbUp,
                            contentDescription = "thumbs up icon",
                            tint = Color.Green.copy(alpha = 0.5f)
                        )
                    }else{
                        Box{}
                    }

                }

            }
            Text(
                text = book.title.toString(),
                modifier = Modifier.padding(4.dp),
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = book.authors.toString(),
                modifier = Modifier.padding(4.dp),
                style = MaterialTheme.typography.labelSmall,
                maxLines = 1,
                overflow = TextOverflow.Clip,
                fontStyle = FontStyle.Italic
            )

            Text(text =  "Started: ${formatDate(book.startedReading!!)}",
                modifier = Modifier.padding(4.dp),
                softWrap = true,
                overflow = TextOverflow.Clip,
                fontStyle = FontStyle.Italic,
                style = MaterialTheme.typography.labelSmall
            )

            Text(text =  "Finished: ${formatDate(book.finishedReading!!)}",
                modifier = Modifier.padding(4.dp),
                overflow = TextOverflow.Clip,
                fontStyle = FontStyle.Italic,
                style = MaterialTheme.typography.labelSmall
            )

        }



    }


}