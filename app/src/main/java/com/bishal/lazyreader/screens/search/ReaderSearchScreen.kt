@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class,
    ExperimentalComposeUiApi::class
)

package com.bishal.lazyreader.screens.search

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme

import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.bishal.lazyreader.components.InputField
import com.bishal.lazyreader.components.ReaderAppBar
import com.bishal.lazyreader.model.MBook
import com.bishal.lazyreader.navigation.ReaderScreen


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ReaderSearchScreen(navController: NavController) {

    Scaffold(topBar = {
        ReaderAppBar(
            title = "Search Books",
            icon = Icons.Default.ArrowBack,
            navController = navController,
            showProfile = false
        ) {
            navController.popBackStack()

        }
    }) {

        Surface() {
            Column {
              SearchForm(
                  modifier = Modifier
                      .fillMaxWidth()
                      .padding(top = 16.dp)){
                  Log.d("TAG", "Searchscreen: $it")

              }
                Spacer(modifier = Modifier.height(13.dp))
                BookList(navController)

            }

        }


    }


}

@Composable
fun BookList(navController: NavController) {
    val listOfBooks = listOf(
        MBook(id = "sdjhk", title = null, authors = "gakhskj", notes = null),
        MBook(id = "sdjhk", title = null, authors = "gakhskj", notes = null),
        MBook(id = "sdjhk", title = null, authors = "gakhskj", notes = null),
        MBook(id = "sdjhk", title = null, authors = "gakhskj", notes = null),
        MBook(id = "sdjhk", title = "oull", authors = "gakhskj", notes = null)
    )

    LazyColumn(modifier = Modifier.fillMaxSize()){
        items(items = listOfBooks){ book ->
            BookRow(book, navController)

        }

    }
}

@Composable
fun BookRow(book: MBook,
            navController: NavController) {
    Card(modifier = Modifier
        .clickable {
            navController.navigate(ReaderScreen.DetailScreen.name + "/${book.id}")
        }
        .fillMaxWidth()
        .height(100.dp)
        .padding(3.dp),
        shape = RectangleShape,
        elevation = CardDefaults.cardElevation(7.dp)) {
        Row(modifier = Modifier.padding(4.dp),
            verticalAlignment = Alignment.Top) {
            val imageUrl = "https://img.freepik.com/free-vector/hand-drawn-flat-design-stack-books-illustration_23-2149330605.jpg?w=2000"
            Image(painter = rememberAsyncImagePainter(model = imageUrl),
                contentDescription = "book",
                modifier = Modifier.width(80.dp)
                    .fillMaxHeight()
                    .padding(end = 4.dp))
            Column {
                Text(text = book.title.toString(), overflow = TextOverflow.Ellipsis)
                Text(text = "Author: ${book.authors}",
                    overflow = TextOverflow.Clip,
                    style = MaterialTheme.typography.labelSmall)
            }

        }

    }

}

@Composable
fun SearchForm(modifier: Modifier = Modifier,
               loading: Boolean = false,
               hint: String = "Search",
               onSearch: (String) -> Unit = {}) {
    Column {
        val searchQueryState = rememberSaveable { mutableStateOf("") }
        val keyboardController = LocalSoftwareKeyboardController.current
        val valid = remember(searchQueryState.value) {
            searchQueryState.value.trim().isNotEmpty()

        }

        InputField(valueState = searchQueryState,
            modifier = modifier.padding(vertical = 45.dp),
            labelId = "Search",
            enabled = true,
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next,
            onAction = KeyboardActions {
                if (!valid) return@KeyboardActions
                onSearch(searchQueryState.value.trim())
                searchQueryState.value = ""
                keyboardController?.hide()
            })

    }

}
