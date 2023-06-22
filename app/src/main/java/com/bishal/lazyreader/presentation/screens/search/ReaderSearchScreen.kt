@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class,
    ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class
)

package com.bishal.lazyreader.presentation.screens.search

import android.annotation.SuppressLint
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
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
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.bishal.lazyreader.presentation.common.BookCategoryChip
import com.bishal.lazyreader.presentation.common.InputField
import com.bishal.lazyreader.presentation.common.LoadingAnimation
import com.bishal.lazyreader.presentation.common.RandomGradientCard
import com.bishal.lazyreader.presentation.common.ReaderAppBar
import com.bishal.lazyreader.domain.model.Item
import com.bishal.lazyreader.navigation.BottomBar
import com.bishal.lazyreader.navigation.ReaderScreen
import kotlinx.coroutines.delay


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


                if (loadState is com.bishal.lazyreader.presentation.screens.search.LoadState.Loading) {
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
    var isLoading by remember { mutableStateOf(true) }
    //val loadState by viewModel.loadState.collectAsState()

   LaunchedEffect(key1 = true){
       delay(2000)
       isLoading = false
   }
    LazyVerticalGrid(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(5.dp),
        state = lazyGridState,
        columns = GridCells.Fixed(2)
    ) {
        items( count = lazyPagingItems.itemCount, // Use itemCount property here
            itemContent = { index ->
                val book = lazyPagingItems[index]
                if (book != null) {
                    BookRow(book = book, navController = navController, isLoading = isLoading, contentAfterLoading = {
                        ContentAfterLoading(
                            book = book,
                            navController = navController
                        )
                    })
                }
            })

        lazyPagingItems.apply {
            when {
                lazyPagingItems.loadState.refresh is LoadState.Loading -> {
                    item { LoadingAnimation() }
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
                    LoadingAnimation(
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
    navController: NavController,
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    contentAfterLoading: @Composable () -> Unit
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
fun Modifier.shimmerEffect(): Modifier = composed {
    var size by remember {
        mutableStateOf(IntSize.Zero)
    }
    val transition = rememberInfiniteTransition()
    val startOffsetX by transition.animateFloat(
        initialValue = -2 * size.width.toFloat(),
        targetValue = 2 * size.width.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(1000)
        )
    )

    background(
        brush = Brush.linearGradient(
            colors = listOf(
                Color(0xFFB8B5B5),
                Color(0xFF8F8B8B),
                Color(0xFFB8B5B5),
            ),
            start = Offset(startOffsetX, 0f),
            end = Offset(startOffsetX + size.width.toFloat(), size.height.toFloat())
        )
    )
        .onGloballyPositioned {
            size = it.size
        }
}

@Composable
fun ContentAfterLoading(
    book: Item,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    RandomGradientCard(
        modifier = modifier
            .clickable {
                navController.navigate(ReaderScreen.DetailScreen.name + "/${book.id}")
            }
            .height(200.dp)
            .padding(3.dp),
        book = book
      )




}