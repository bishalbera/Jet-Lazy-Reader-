@file:OptIn(ExperimentalMaterialApi::class)

package com.bishal.lazyreader.screens.details

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.core.animateDpAsState
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
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material.rememberBottomSheetState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.bishal.lazyreader.components.RoundedButton
import com.bishal.lazyreader.data.Resource
import com.bishal.lazyreader.model.Item
import com.bishal.lazyreader.model.MBook
import com.bishal.lazyreader.ui.theme.EXPANDED_RADIUS_LEVEL
import com.bishal.lazyreader.ui.theme.EXTRA_LARGE_PADDING
import com.bishal.lazyreader.ui.theme.INFO_ICON_SIZE
import com.bishal.lazyreader.ui.theme.MIN_SHEET_HEIGHT
import com.bishal.lazyreader.ui.theme.SMALL_PADDING
import com.bishal.lazyreader.ui.theme.titleColor
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.net.URLEncoder

@SuppressLint("SuspiciousIndentation")
@Composable
fun DetailsContent(
    navController: NavController,
    viewModel: ReaderDetailViewModel = hiltViewModel(),
    colors: Map<String, String>,
    bookId: String,
    bookInfo: Resource<Item>,


    ) {



    var vibrant by remember { mutableStateOf("#000000") }
    var darkVibrant by remember { mutableStateOf("#000000") }
    var lightVibrant by remember { mutableStateOf("#000000") }
    var domainSwatch by remember { mutableStateOf("#000000") }
    var mutedSwatch by remember { mutableStateOf("#000000") }
    var lightMutedSwatch by remember { mutableStateOf("#000000") }
    var darkMutedSwatch by remember { mutableStateOf("#000000") }
    var onDarkVibrant by remember { mutableStateOf("#ffffff") }
    LaunchedEffect(key1 = true) {
        vibrant = colors["vibrant"].toString()
        darkVibrant = colors["darkVibrant"].toString()
        lightVibrant = colors["lightVibrant"].toString()
        domainSwatch = colors["domainSwatch"].toString()
        mutedSwatch = colors["mutedSwatch"].toString()
        lightMutedSwatch = colors["lightMuted"].toString()
        darkMutedSwatch = colors["darkMuted"].toString()
        onDarkVibrant = colors["onDarkVibrant"].toString()
    }


    val systemUiController = rememberSystemUiController()
//    systemUiController.setStatusBarColor(
//       color =  Color(parseColor(darkMutedSwatch))
//
//
//
//    )

    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberBottomSheetState(initialValue = BottomSheetValue.Expanded)
    )
    val currentSheetFraction = scaffoldState.currentSheetFraction

    val radiusAnim by animateDpAsState(
        targetValue =
        if (currentSheetFraction == 1f)
            EXTRA_LARGE_PADDING
        else
            EXPANDED_RADIUS_LEVEL
    )

//    val bookInfo = produceState<Resource<Item>>(initialValue = Resource.Loading()){
//        value = viewModel.getBookInfo(bookId)
//    }.value



        BottomSheetScaffold(
            sheetShape = RoundedCornerShape(
                topStart = 11.dp,
                topEnd = 11.dp
            ),
            scaffoldState = scaffoldState,
            sheetPeekHeight = MIN_SHEET_HEIGHT,
            sheetContent = {

                ShowBookDetails(
                    bookInfo = bookInfo,
                    navController = navController,
                    sheetBackgroundColor = MaterialTheme.colorScheme.surface,
                   // contentColor = Color(parseColor(onDarkVibrant)),
                    //iconColor = Color(parseColor(vibrant))
                )
            },
            content = {
                BackgroundContent(
                    bookImage = bookInfo.data?.volumeInfo?.imageLinks?.thumbnail,
                    imageFraction = currentSheetFraction,
                   // backgroundColor = Color(parseColor(darkVibrant)),
                    bookId = bookId,
                    onCloseClicked = {
                        navController.popBackStack()
                    }

                )
            }
        )



}

@Composable
fun BackgroundContent(
    bookImage: String?,
    imageFraction: Float = 1f,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    onCloseClicked: () -> Unit,
    bookId: String,
    viewModel: ReaderDetailViewModel = hiltViewModel()
) {


    val bookInfo = produceState<Resource<Item>>(initialValue = Resource.Loading()){
        value = viewModel.getBookInfo(bookId)
    }.value

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
                    .padding(SMALL_PADDING),
                onClick = { onCloseClicked() }
            ) {
                Icon(
                    modifier = Modifier
                        .size(INFO_ICON_SIZE),
                    imageVector = Icons.Default.Close,
                    contentDescription = "close",
                    tint = Color.White
                )

            }

        }
    }

}

@Composable
fun ShowBookDetails(
    bookInfo: Resource<Item>,
    navController: NavController,
    sheetBackgroundColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = androidx.compose.material.MaterialTheme.colors.titleColor,
    iconColor: Color = MaterialTheme.colorScheme.primary
) {
    val bookData = bookInfo.data?.volumeInfo
    val googleBookId = bookInfo.data?.id

    Surface(modifier = Modifier
        .fillMaxWidth()

    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(sheetBackgroundColor),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = bookData?.title.toString(),
                style = MaterialTheme.typography.headlineMedium,
                color = contentColor,
                fontWeight = FontWeight.Bold,
                overflow = TextOverflow.Ellipsis,
                maxLines = 5
            )
            Text(
                text = "Authors: ${bookData?.authors.toString()}",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold,
                color = contentColor
            )
            Text(
                text = "Published On: ${bookData?.publishedDate.toString()}",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Thin,
                color = contentColor
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
                        tint = iconColor

                        )
                    Text(text = bookData?.averageRating.toString(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = contentColor
                    )

                    Spacer(modifier = Modifier.width(15.dp))


                    Text(text = "PageCount: ${bookData?.pageCount.toString()}",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp,
                        color = contentColor
                    )

                    Spacer(modifier = Modifier.width(15.dp))

                    Text(text = "Categories: ${bookData?.categories.toString()}",
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        fontSize = 12.sp,
                        overflow = TextOverflow.Ellipsis,
                        color = contentColor
                    )

                }
            }

            val cleanDescription = HtmlCompat.fromHtml(
                bookData?.description.toString(),
                HtmlCompat.FROM_HTML_MODE_LEGACY).toString()

            Text(
                text = "Description:",
                fontWeight = FontWeight.Bold,
                color = contentColor,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(top = 12.dp, start = 12.dp, end = 12.dp))
            Text(
                text = cleanDescription,
                modifier = Modifier
                    .padding(12.dp),
                maxLines = 10,
                overflow = TextOverflow.Ellipsis,
                color = contentColor
            )

            //Buttons
            Row(modifier = Modifier.padding(top = 6.dp, bottom = 6.dp),
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
                Spacer(modifier = Modifier.width(23.dp))
                val context = LocalContext.current
                RoundedButton(label = "Search Book"){

                    openInExternalBrowserWithBookSearchQuery(bookData?.title, context = context)
                }

            }


        }


    }


}

fun openInExternalBrowserWithBookSearchQuery(title: String?, context: Context) {
    val searchQueryUrl = generateSearchQueryUrl(title)

    val context = context

    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(searchQueryUrl))
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

    val packageManager = context.packageManager
    if (intent.resolveActivity(packageManager) !=null){
        context.startActivity(intent)
    }else{
        // Handle case when no browser app is available on the device
        Toast.makeText(context, "No browser app found", Toast.LENGTH_SHORT).show()
    }


}

fun generateSearchQueryUrl(title: String?): String? {
    val encodedTitle = URLEncoder.encode(title, "UTF-8")
    return "https://www.google.com/search?q=$encodedTitle+book+pdf+download"
}



@OptIn(ExperimentalMaterialApi::class)
val BottomSheetScaffoldState.currentSheetFraction: Float
    get() {

        return when (val progress = bottomSheetState.progress) {
            0f -> 1f
            1f -> 0f
            else -> 1f - progress
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


