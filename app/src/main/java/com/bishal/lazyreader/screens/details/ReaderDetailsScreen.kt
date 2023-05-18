package com.bishal.lazyreader.screens.details

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.text.HtmlCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.bishal.lazyreader.components.ReaderAppBar
import com.bishal.lazyreader.components.RoundedButton
import com.bishal.lazyreader.data.Resource
import com.bishal.lazyreader.model.Item
import com.bishal.lazyreader.model.MBook
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

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

    Box(modifier = Modifier.padding(1.dp)
        .fillMaxSize(0.5f),
     
        ) {
        Image(painter = rememberAsyncImagePainter(model = bookData?.imageLinks?.thumbnail),
            contentDescription = "Book Image",
            modifier = Modifier
                .size(400.dp)
                .padding(1.dp),

            contentScale = ContentScale.FillBounds,
        alpha = 0.5f)
        Image(painter = rememberAsyncImagePainter(model = bookData?.imageLinks?.thumbnail),
            contentDescription = "Book Image",
            modifier = Modifier
                .height(190.dp)
                .width(150.dp)
                .padding(1.dp)
                .align(Alignment.Center),

            contentScale = ContentScale.FillBounds,
            alpha = 1.0f)

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
    //Buttons

    Row(modifier = Modifier.padding(top = 6.dp),
        horizontalArrangement = Arrangement.SpaceAround) {
        RoundedButton(label = "Save"){
            //save this book to the firestore database
            val book = MBook(
                title = bookData?.title,
                authors = bookData?.authors.toString(),
                description = bookData?.description,
                categories = bookData?.categories.toString(),
                notes = "",
                photoUrl = bookData?.imageLinks?.thumbnail,
                publishedDate = bookData?.publishedDate,
                pageCount = bookData?.pageCount.toString(),
                rating = 0.0,
                googleBookId = googleBookId,
                userId = FirebaseAuth.getInstance().currentUser?.uid.toString())

            saveToFirebase(book, navController = navController)

        }
        Spacer(modifier = Modifier.width(25.dp))
        RoundedButton(label = "Cancel"){
            navController.popBackStack()
        }

    }


}



fun saveToFirebase(book: MBook, navController: NavController) {
    val db = FirebaseFirestore.getInstance()
    val dbCollection = db.collection("books")

    if (book.toString().isNotEmpty()){
        dbCollection.add(book)
            .addOnSuccessListener { documentRef ->
                val docId = documentRef.id
                dbCollection.document(docId)
                    .update(hashMapOf("id" to docId) as Map<String, Any>)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            navController.popBackStack()
                        }


                    }.addOnFailureListener {
                        Log.w("Error", "SaveToFirebase:  Error updating doc",it )
                    }

            }


    }else {



    }

}
