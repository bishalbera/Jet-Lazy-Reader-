@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class
)

package com.bishal.lazyreader.components

import android.view.MotionEvent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AppBarDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.ColorUtils
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.bishal.lazyreader.R
import com.bishal.lazyreader.model.Item
import com.bishal.lazyreader.model.MBook
import com.bishal.lazyreader.navigation.ReaderScreen
import com.bishal.lazyreader.ui.theme.BlueViolet1
import com.bishal.lazyreader.ui.theme.BlueViolet2
import com.bishal.lazyreader.ui.theme.BlueViolet3
import com.bishal.lazyreader.utils.standardQuadFromTo
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import java.util.Random

@Composable
fun EmailInput(
    modifier: Modifier = Modifier,
    emailState: MutableState<String>,
    labelId: String = "Email",
    enabled: Boolean = false,
    imeAction: ImeAction = ImeAction.Next,
    onAction: KeyboardActions = KeyboardActions.Default
){
    InputField(
        modifier = modifier,
        valueState = emailState,
        labelId = labelId,
        enabled = enabled,
        keyboardType = KeyboardType.Email,
        imeAction = imeAction,
        onAction = onAction)
}

@Composable
fun InputField(
    modifier: Modifier,
    valueState: MutableState<String>,
    labelId: String,
    enabled: Boolean,
    keyboardType: KeyboardType,
    imeAction: ImeAction,
    onAction: KeyboardActions,
    placeholder: String? = null,
    leadingIcon: Unit? = null
) {
    val containerColor = Color.LightGray.copy(alpha = 0.4f)
    OutlinedTextField(value = valueState.value,
        onValueChange = {valueState.value= it},
        label = {Text(text = labelId)},
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = containerColor,
            unfocusedContainerColor = containerColor,
            disabledContainerColor = containerColor,
            cursorColor = Color(0xFFE69360),
            focusedBorderColor = Color(0xFF203226),
            disabledBorderColor = Color(0xFF203226),
            focusedLabelColor = Color(0xFFE69360),
            unfocusedLabelColor = Color(0xFFE69360),
        ),
        textStyle = TextStyle(fontSize = 18.sp,
            color = Color.Black),
        modifier = modifier
            .padding(bottom = 10.dp, start = 10.dp, end = 10.dp)
            .fillMaxWidth(),
        enabled = enabled,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
        keyboardActions = onAction,
        placeholder = {
            if (placeholder != null) {
                Text(text = placeholder)
            }
        },
        leadingIcon = { Icon(imageVector = Icons.Filled.Search, contentDescription ="search icon" )}
    )


}

@Composable
fun PasswordInput(
    modifier: Modifier,
    passwordState: MutableState<String>,
    labelId: String,
    enabled: Boolean,
    passwordVisibility: MutableState<Boolean>,
    imeAction: ImeAction = ImeAction.Done,
    onAction: KeyboardActions = KeyboardActions.Default
){
    val visualTransformation = if (passwordVisibility.value) VisualTransformation.None else
        PasswordVisualTransformation()
    OutlinedTextField(value = passwordState.value,
        onValueChange = {passwordState.value = it},
        label = { Text(text = labelId)},
        singleLine = true,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            containerColor = Color.LightGray.copy(alpha = 0.4f),
            cursorColor = Color(0xFFE69360),
            disabledBorderColor = Color(0xFF203226),
            focusedBorderColor = Color(0xFF203226),
            focusedLabelColor = Color(0xFFE69360),
            unfocusedLabelColor = Color(0xFFE69360)
        ),
        modifier = modifier
            .padding(bottom = 10.dp, start = 10.dp, end = 10.dp)
            .fillMaxWidth(),
        enabled = enabled,
    textStyle = TextStyle(
        fontSize = 18.sp,
        color = Color.Black
    ),
    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password,
                                    imeAction = imeAction),
    visualTransformation = visualTransformation,
    trailingIcon = {PasswordVisibility(passwordVisibility = passwordVisibility)},
    keyboardActions = onAction)

}
@Composable
fun PasswordVisibility(passwordVisibility: MutableState<Boolean>){
    val visible = passwordVisibility.value
    IconButton(onClick = { passwordVisibility.value = !visible }) {
        Icons.Default.Close

    }

}

@Composable
fun ReaderAppBar(
    title: String,
    icon: ImageVector? = null,
    showProfile: Boolean = true,
    navController: NavController,
    onBackArrowClicked:() -> Unit = {}
){
    Surface(color = BlueViolet2,
    tonalElevation = AppBarDefaults.TopAppBarElevation) {
        TopAppBar(
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (showProfile) {
                        Icon(imageVector = Icons.Default.Book, contentDescription = "logo",
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .scale(0.9f)
                        )
                    }
                    if (icon !=null) {
                        Icon(imageVector = icon, contentDescription = "arrowback",
                            tint = Color.Red.copy(alpha = 0.7f),
                            modifier = Modifier.clickable { onBackArrowClicked.invoke() })
                    }
                    Spacer(modifier = Modifier.width(40.dp))
                    Text(text = title,
                        color = Color.Red.copy(alpha = 0.7f),
                        style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp))
                }
            },
            actions = {
                IconButton(onClick = { FirebaseAuth.getInstance()
                    .signOut().run {
                        navController.navigate(ReaderScreen.LoginScreen.name)
                    }
                }) {
                    if (showProfile) Row() {
                        Icon(imageVector = Icons.Filled.Logout, contentDescription = "logout",)


                    }else Box {}


                }
            },

        colors = TopAppBarDefaults.topAppBarColors(BlueViolet2)



        )
    }

        

    
}



@Composable
fun TitleSection(modifier: Modifier = Modifier,
                label: String){
    Surface(modifier = modifier.padding(start = 5.dp, top = 1.dp)) {
        Column {
            Text(text = label,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Left)

        }
    }


}


@Composable
fun ListCard(book: MBook,
             onPressDetails: (String) -> Unit = {}) {
    val context = LocalContext.current
    val resources = context.resources

    val displayMetrics = resources.displayMetrics

    val screenWidth = displayMetrics.widthPixels / displayMetrics.density
    val spacing = 10.dp

    Card(shape = RoundedCornerShape(29.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        modifier = Modifier
            .padding(16.dp)
            .height(236.dp)
            .width(202.dp)
            .clickable { onPressDetails.invoke(book.title.toString()) }) {

        Column(modifier = Modifier.width(screenWidth.dp - (spacing * 2)),
            horizontalAlignment = Alignment.Start) {
            Row(horizontalArrangement = Arrangement.Center) {

                Image(painter = rememberAsyncImagePainter(model = book.photoUrl.toString()),
                    contentDescription = "book image",
                    modifier = Modifier
                        .height(140.dp)
                        .width(100.dp)
                        .padding(4.dp))
                Spacer(modifier = Modifier.width(50.dp))

                Column(modifier = Modifier.padding(top = 25.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    BookRating(score = book.rating!!)               }

            }
            Text(text = book.title.toString(), modifier = Modifier.padding(4.dp),
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis)

            Text(text = book.authors.toString(), modifier = Modifier.padding(4.dp),
                style = MaterialTheme.typography.titleSmall) }

        val isStartedReading = remember {
            mutableStateOf(false)
        }

        Row(horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.Bottom) {
            isStartedReading.value = book.startedReading != null


            RoundedButton(label = if (isStartedReading.value)  "Reading" else
                "Not Started",
                radius = 70)

        }
    }



}



@Composable
fun BookRating(score: Double){
    Surface(modifier = Modifier
        .height(70.dp)
        .padding(4.dp),
    shape = RoundedCornerShape(56.dp),
        color = Color.White,
        tonalElevation = 6.dp
    ) {
        Column(modifier = Modifier.padding(4.dp)) {
            Icon(imageVector = Icons.Filled.StarBorder,
                contentDescription = "Star",
                modifier = Modifier.padding(3.dp))
            Text(text = score.toString(),
                style = MaterialTheme.typography.labelSmall)

        }

    }

}

@Composable
fun RoundedButton(
    label: String = "Reading",
    radius: Int = 29,
    onPress: () -> Unit = {}) {
    Surface(modifier = Modifier.clip(RoundedCornerShape(
        bottomEndPercent = radius,
        topStartPercent = radius)),
        color = Color(0xFF92CBDF)) {

        Column(modifier = Modifier
            .width(90.dp)
            .heightIn(40.dp)
            .clickable { onPress.invoke() },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = label, style = TextStyle(color = Color.White,
                fontSize = 15.sp),)

        }

    }


}

//Rating Bar
@ExperimentalComposeUiApi
@Composable
fun RatingBar(
    modifier: Modifier = Modifier,
    rating: Int,
    onPressRating: (Int) -> Unit
) {
    var ratingState by remember {
        mutableStateOf(rating)
    }

    var selected by remember {
        mutableStateOf(false)
    }
    val size by animateDpAsState(
        targetValue = if (selected) 42.dp else 34.dp,
        spring(Spring.DampingRatioMediumBouncy)
    )

    Row(
        modifier = Modifier.width(280.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        for (i in 1..5) {
            Icon(
                painter = painterResource(id = R.drawable.ic_baseline_star_24),
                contentDescription = "star",
                modifier = modifier
                    .width(size)
                    .height(size)
                    .pointerInteropFilter {
                        when (it.action) {
                            MotionEvent.ACTION_DOWN -> {
                                selected = true
                                onPressRating(i)
                                ratingState = i
                            }

                            MotionEvent.ACTION_UP -> {
                                selected = false
                            }
                        }
                        true
                    },
                tint = if (i <= ratingState) Color(0xFFFFD700) else Color(0xFFA2ADB1)
            )
        }
    }
}

@Composable
fun BookCategoryChip(
    category: String,
    onExecuteSearch: (String) -> Unit
) {
    Surface(
        modifier = Modifier
            .padding(end = 8.dp),
        shadowElevation = 8.dp,
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.primary
    ) {
        Row(
            modifier = Modifier
                .clickable { onExecuteSearch(category) }
        ) {
            Text(
                text = category,
                style = MaterialTheme.typography.labelMedium,
                color = Color.White,
                modifier = Modifier.padding(8.dp)
            )

        }

    }
}

@Composable
fun LoadingAnimation(
    modifier: Modifier = Modifier,
    circleSize: Dp = 25.dp,
    circleColor: Color = MaterialTheme.colorScheme.primary,
    spaceBetween: Dp = 10.dp,
    travelDistance: Dp = 20.dp
) {
    val circles = listOf(
        remember { Animatable(initialValue = 0f) },
        remember { Animatable(initialValue = 0f) },
        remember { Animatable(initialValue = 0f) }
    )

    circles.forEachIndexed { index, animatable ->
        LaunchedEffect(key1 = animatable ){
            delay(index * 100L)
            animatable.animateTo(
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = keyframes {
                        durationMillis = 1200
                        0.0f at 0 with LinearOutSlowInEasing
                        1.0f at 300 with LinearOutSlowInEasing
                        0.0f at 600 with LinearOutSlowInEasing
                        0.0f at 1200 with LinearOutSlowInEasing
                    },
                    repeatMode = RepeatMode.Restart
                )
            )

        }
    }

    val circleValues = circles.map { it.value }
    val distance = with(LocalDensity.current) {travelDistance.toPx()}

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(spaceBetween)
    ) {
        circleValues.forEach { value ->
            Box(
                modifier = Modifier
                    .size(circleSize)
                    .graphicsLayer {
                        translationY = -value * distance
                    }
                    .background(
                        color = circleColor,
                        shape = CircleShape
                    )
            ) {

            }

        }

    }

}





@Composable
fun RandomGradientCard(modifier: Modifier, book: Item) {
    val randomColor = remember { generateRandomColor() }
    val gradientColors = generateGradientColors(randomColor)

    BoxWithConstraints(
        modifier = Modifier
            .padding(7.dp)
            .aspectRatio(1f)
            .clip(RoundedCornerShape(10.dp))
            .background(BlueViolet3)
    ) {
        val width = constraints.maxWidth
        val height = constraints.maxHeight

        //Medium colored path
        val mediumColoredPoint1 = Offset(0f, height * 0.3f)
        val mediumColoredPoint2 = Offset(width * 0.1f, height * 0.35f)
        val mediumColoredPoint3 = Offset(width * 0.4f, height * 0.05f)
        val mediumColoredPoint4 = Offset(width * 0.75f, height * 0.7f)
        val mediumColoredPoint5 = Offset(width * 1.4f, -height.toFloat())

        val mediumColoredPath = Path().apply {
            moveTo(mediumColoredPoint1.x, mediumColoredPoint1.y)
            standardQuadFromTo(mediumColoredPoint1, mediumColoredPoint2)
            standardQuadFromTo(mediumColoredPoint2, mediumColoredPoint3)
            standardQuadFromTo(mediumColoredPoint3, mediumColoredPoint4)
            standardQuadFromTo(mediumColoredPoint4, mediumColoredPoint5)
            lineTo(width.toFloat() + 100f, height.toFloat() + 100f)
            lineTo(-100f, height.toFloat() + 100f)
            close()
        }

        // Light colored path
        val lightPoint1 = Offset(0f, height * 0.35f)
        val lightPoint2 = Offset(width * 0.1f, height * 0.4f)
        val lightPoint3 = Offset(width * 0.3f, height * 0.35f)
        val lightPoint4 = Offset(width * 0.65f, height.toFloat())
        val lightPoint5 = Offset(width * 1.4f, -height.toFloat() / 3f)

        val lightColoredPath = Path().apply {
            moveTo(lightPoint1.x, lightPoint1.y)
            standardQuadFromTo(lightPoint1, lightPoint2)
            standardQuadFromTo(lightPoint2, lightPoint3)
            standardQuadFromTo(lightPoint3, lightPoint4)
            standardQuadFromTo(lightPoint4, lightPoint5)
            lineTo(width.toFloat() + 100f, height.toFloat() + 100f)
            lineTo(-100f, height.toFloat() + 100f)
            close()
        }

        Canvas(
            modifier = Modifier
                .fillMaxSize()
        ) {
            drawPath(
                path = mediumColoredPath,
                color = BlueViolet2
            )
            drawPath(
                path = lightColoredPath,
                color = BlueViolet1
            )
        }

           Box(
               modifier = Modifier
                   .padding(5.dp)
                   .fillMaxSize()
           ) {
               Row(
                   verticalAlignment = Alignment.Top,
                   modifier = Modifier.padding(5.dp)
               ) {
                   val imageUrl = if(book.volumeInfo.readingModes.image){
                       book.volumeInfo.imageLinks?.smallThumbnail
                   }
                   else { "https://images.unsplash.com/photo-1541963463532-d68292c34b19?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=80&q=80" }
                   Image(
                       painter = rememberAsyncImagePainter(model = imageUrl),
                       contentDescription = "book image",
                       modifier = modifier
                           .width(80.dp)
                           .fillMaxHeight()
                           .padding(end = 4.dp)
                   )

                   Column {
                       Text(
                           text = book.volumeInfo.title,
                           overflow = TextOverflow.Ellipsis,
                           maxLines = 1,
                           style = MaterialTheme.typography.labelMedium
                       )
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
}

fun generateGradientColors(color: Color): List<Color> {
    val gradientColors = mutableListOf<Color>()
    gradientColors.add(color)

    val hsv = FloatArray(3)
    ColorUtils.colorToHSL(color.value.toInt(), hsv)

    val mediumShade = ColorUtils.HSLToColor(hsv)
    gradientColors.add(Color(mediumShade))

    // Generate dark shade by decreasing brightness
    hsv[2] -= 0.1f
    val darkShade = ColorUtils.HSLToColor(hsv)
    gradientColors.add(Color(darkShade))

    // Generate light shade by increasing brightness
    hsv[2] += 0.2f
    val lightShade = ColorUtils.HSLToColor(hsv)
    gradientColors.add(Color(lightShade))

    return gradientColors
}


fun generateRandomColor(): Color {
    val random = Random(System.currentTimeMillis())
    return Color(random.nextInt(256), random.nextInt(256), random.nextInt(256))

}
