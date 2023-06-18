@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class,
    ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class
)

package com.bishal.lazyreader.screens.search

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.rememberAsyncImagePainter
import com.bishal.lazyreader.components.BookCategoryChip
import com.bishal.lazyreader.components.InputField
import com.bishal.lazyreader.components.ReaderAppBar
import com.bishal.lazyreader.model.Item
import com.bishal.lazyreader.navigation.BottomBar
import com.bishal.lazyreader.navigation.ReaderScreen




@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@ExperimentalComposeUiApi
@Composable
fun ReaderSearchScreen(
    navController: NavController,
    viewModel: ReaderSearchScreenViewModel = hiltViewModel(),
) {

    Scaffold(

        topBar = {
            ReaderAppBar(
                title = "Search Books",
                icon = Icons.Default.ArrowBack,
                navController = navController,
                showProfile = false,
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
        Surface {
//            val query by viewModel.query.collectAsState()
//            val items by viewModel.items.collectAsState()
            val loadState by viewModel.loadState.collectAsState()
            val items = viewModel.searchResults.collectAsLazyPagingItems()
            Column {
                SearchForm(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(13.dp)){ searchQuery ->
                    viewModel.search(searchQuery)

                }
                BookCategoriesRow(
                    categories = getAllBookCategories(),
                    onItemClick = { category ->
                        viewModel.search(category)

                    }

                )


                Spacer(modifier = Modifier.height(13.dp))


                if (loadState is com.bishal.lazyreader.screens.search.LoadState.Loading) {
                    LinearProgressIndicator()
                }  else {
                    BookList(navController)
                }


            }



        }
    }

}

@Composable
fun BookCategoriesRow(
    categories: List<BookCategory>,
    onItemClick: (String) -> Unit

) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
    ){items(categories){ category ->
        BookCategoryChip(
            category = category.value,
            onExecuteSearch = {
                onItemClick(it)
            }
        )

    }

    }
}


@Composable
fun BookList(
    navController: NavController,
    viewModel: ReaderSearchScreenViewModel = hiltViewModel()
) {
    val lazyPagingItems = viewModel.searchResults.collectAsLazyPagingItems()
    val lazyListState = rememberLazyListState()
    val lazyGridState = rememberLazyGridState()
    //val loadState by viewModel.loadState.collectAsState()

    LazyVerticalGrid(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp),
        state = lazyGridState,
        columns = GridCells.Fixed(2)
    ) {
        items( count = lazyPagingItems.itemCount, // Use itemCount property here
            itemContent = { index ->
                val book = lazyPagingItems[index]
                if (book != null) {
                    BookRow(book = book, navController = navController)
                }
            })

        lazyPagingItems.apply {
            when {
                lazyPagingItems.loadState.refresh is LoadState.Loading -> {
                    item { CircularProgressIndicator() }
                }
                loadState.refresh is LoadState.Error -> {
                    val errorMessage = (loadState.refresh as LoadState.Error)
                    item {
                        Text(text = "Error: $errorMessage")
                    }
                }
            }

            // Load more items when scrolled to the end
            if (loadState.append is LoadState.Loading) {
                item {
                    LinearProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )
                }
            }
            if (loadState.append is LoadState.Error) {
                val errorMessage = (loadState.append as LoadState.Error)
                item {
                    Text(
                        text = "Error: $errorMessage",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )
                }
            }
        }
    }
}


@Composable
fun BookRow(
    book: Item,
    navController: NavController) {
    Card(modifier = Modifier
        .clickable {
            navController.navigate(ReaderScreen.DetailScreen.name + "/${book.id}")
        }
        .fillMaxWidth()
        .height(200.dp)
        .padding(3.dp),
        shape = RoundedCornerShape(13.dp),
        elevation = CardDefaults.cardElevation(7.dp)) {
        Row(modifier = Modifier.padding(5.dp),
            verticalAlignment = Alignment.Top) {

            val imageUrl = if(book.volumeInfo.readingModes.image){
                book.volumeInfo.imageLinks?.smallThumbnail
            }
            else { "https://images.unsplash.com/photo-1541963463532-d68292c34b19?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=80&q=80" }
            Image(
                painter = rememberAsyncImagePainter(model = imageUrl),
                contentDescription = "book image",
                modifier = Modifier
                    .width(80.dp)
                    .fillMaxHeight()
                    .padding(end = 4.dp),
            )

            Column {
                Text(text = book.volumeInfo.title, overflow = TextOverflow.Ellipsis)
                Text(text =  "Author: ${book.volumeInfo.authors}",
                    overflow = TextOverflow.Clip,
                    fontStyle = FontStyle.Italic,
                    style = MaterialTheme.typography.labelSmall)

                Text(text =  "Date: ${book.volumeInfo.publishedDate}",
                    overflow = TextOverflow.Clip,
                    fontStyle = FontStyle.Italic,
                    style = MaterialTheme.typography.labelSmall)

                Text(text =  "${book.volumeInfo.categories}",
                    overflow = TextOverflow.Clip,
                    fontStyle = FontStyle.Italic,
                    style = MaterialTheme.typography.labelSmall)






            }

        }

    }

}


@ExperimentalComposeUiApi
@Composable
fun SearchForm(
    modifier: Modifier = Modifier,
    loading: Boolean = false,
    hint: String = "Search",
    onSearch: (String) -> Unit = {}
) {
    Column {
        val searchQueryState = rememberSaveable { mutableStateOf("") }
        val keyboardController = LocalSoftwareKeyboardController.current
        val valid = remember(searchQueryState.value) {
            searchQueryState.value.trim().isNotEmpty()

        }


        InputField(
            valueState = searchQueryState,
            modifier = modifier
                .padding(vertical = 25.dp)
                .fillMaxWidth(),
            labelId = "Search",
            enabled = true,
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next,
            onAction = KeyboardActions {
                if (!valid) return@KeyboardActions
                onSearch(searchQueryState.value.trim())
                searchQueryState.value = ""
                keyboardController?.hide()
            },
            placeholder = "Search for Books, Novels..",
            leadingIcon = Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = "search icon"
            )

        )


    }
}


