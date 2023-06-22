@file:OptIn(ExperimentalMaterialApi::class)

package com.bishal.lazyreader.presentation.screens.details

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetScaffoldState
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material.rememberBottomSheetState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.HtmlCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.bishal.lazyreader.data.Resource
import com.bishal.lazyreader.domain.model.Item
import com.bishal.lazyreader.domain.model.MBook
import com.bishal.lazyreader.presentation.common.RoundedButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.net.URLEncoder

@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")

@Composable
fun ReaderDetailsScreen(
    navController: NavController,
    viewModel: ReaderDetailViewModel = hiltViewModel(),
    bookId: String
) {
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberBottomSheetState(initialValue = BottomSheetValue.Expanded)
    )
    val currentSheetFraction = scaffoldState.currentSheetFraction

    val bookInfo = produceState<Resource<Item>>(initialValue = Resource.Loading()){
        value = viewModel.getBookInfo(bookId)
    }.value

    BottomSheetScaffold(
        sheetShape = RoundedCornerShape(
            topStart = 11.dp,
            topEnd = 11.dp
        ),
        scaffoldState = scaffoldState,
        sheetPeekHeight = 140.dp,
        sheetContent = {

            ShowBookDetails(
                bookInfo = bookInfo,
                navController = navController
            )
        },
        content = {
            BackgroundContent(
                imageFraction = currentSheetFraction,
                bookInfo = bookInfo
            ) {
                navController.popBackStack()
            }
        }
    )

}

@Composable
fun BackgroundContent(
    imageFraction: Float,
    bookInfo: Resource<Item>,
    onCloseClicked: () -> Unit
) {

    val bookData = bookInfo.data?.volumeInfo
    val imageUrl = bookData?.imageLinks?.thumbnail
    val painter = rememberAsyncImagePainter(model = imageUrl)


    Box(modifier = Modifier
        .fillMaxSize()

    ) {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(fraction = imageFraction + 0.8f)
                .align(Alignment.TopStart),
            painter = painter,
            contentDescription = "book image",
            contentScale = ContentScale.Crop
        )
        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(
                modifier = Modifier
                    .padding(10.dp),
                onClick = { onCloseClicked() }
            ) {
                Icon(
                    modifier = Modifier
                        .size(32.dp),
                    imageVector = Icons.Default.Close,
                    contentDescription = "close",
                    tint = MaterialTheme.colorScheme.onTertiary
                )

            }

        }
    }

}

@Composable
fun ShowBookDetails(
    bookInfo: Resource<Item>,
    navController: NavController
) {
    val bookData = bookInfo.data?.volumeInfo
    val googleBookId = bookInfo.data?.id
    val brushColor: List<Color> = listOf( Color(0xff227C70),
        Color(0xffC92C6D))


    Surface(modifier = Modifier
        .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = brushColor, startY = 30f, endY = 830f
                    )
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = bookData?.title.toString(),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold,
                overflow = TextOverflow.Ellipsis,
                maxLines = 5
            )
            Text(
                text = "Authors: ${bookData?.authors.toString()}",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.background
            )
            Text(
                text = "Published On: ${bookData?.publishedDate.toString()}",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onBackground
            )


            Box(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth()
                    .background(
                        color = Color.LightGray,
                        shape = RoundedCornerShape(12.dp)

                    )

            ) {
                Row(
                    modifier = Modifier
                        .padding(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = "star",
                        tint = Color(0xffFFD93D)

                    )
                    Text(text = bookData?.averageRating.toString(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )

                    Spacer(modifier = Modifier.width(15.dp))


                    Text(text = "PageCount: ${bookData?.pageCount.toString()}",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )

                    Spacer(modifier = Modifier.width(15.dp))

                    Text(text = "Categories: ${bookData?.categories.toString()}",
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        fontSize = 12.sp,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.onPrimary
                    )

                }
            }

            val cleanDescription = HtmlCompat.fromHtml(
                bookData?.description.toString(),
                HtmlCompat.FROM_HTML_MODE_LEGACY).toString()

            Text(
                text = "Description:",
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(top = 12.dp, start = 12.dp, end = 12.dp))
            Text(
                text = cleanDescription,
                modifier = Modifier
                    .padding(12.dp),
                maxLines = 10,
                overflow = TextOverflow.Ellipsis,
            )


            //Buttons
            Row(modifier = Modifier.padding(top = 6.dp, bottom = 6.dp),
                horizontalArrangement = Arrangement.SpaceAround) {
                RoundedButton(label = "Save"){
                    //save this book to the appwrite database
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

                    saveToFirebase( book, navController = navController)



                }
                Spacer(modifier = Modifier.width(25.dp))
                RoundedButton(label = "Cancel"){
                    navController.popBackStack()
                }
                Spacer(modifier = Modifier.width(23.dp))
                val context = LocalContext.current
                RoundedButton("Search Book"){
                    openInExternalBrowserWithBookSearchQuery(bookData?.title, context = context)
                }

            }


        }


    }

}

@SuppressLint("QueryPermissionsNeeded")
fun openInExternalBrowserWithBookSearchQuery(
    title: String?,
    context: Context
) {
    val searchQueryUrl = generateSearchQueryUrl(title)

    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(searchQueryUrl))
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

    val packageManager = context.packageManager
    val resolvedIntentActivities = packageManager.queryIntentActivities(intent, 0)
    if (resolvedIntentActivities.isNotEmpty()){
        val chooserIntent = Intent.createChooser(intent, "Open with")

        chooserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(chooserIntent)
    }else{
        // Handle case when no browser app is available on the device
        Toast.makeText(context, "No browser app found", Toast.LENGTH_SHORT).show()
    }


}

fun generateSearchQueryUrl(title: String?): String? {
    val encodedTitle = URLEncoder.encode(title, "UTF-8")
    return "https://www.google.com/search?q=$encodedTitle+book+pdf+download"
}


fun saveToFirebase(
    book: MBook,
    navController: NavController
) {
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


    }else {}


}


val BottomSheetScaffoldState.currentSheetFraction: Float
    get() {

        return when (val progress = bottomSheetState.progress) {
            0f -> 1f
            1f -> 0f
            else -> 1f - progress
        }
    }