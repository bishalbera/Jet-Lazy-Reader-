package com.bishal.lazyreader.screens.lottie


import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.bishal.lazyreader.R
import com.bishal.lazyreader.navigation.ReaderScreen
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun ReaderLottieScreen(navController: NavController){


    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.lottie))


    Surface(modifier = Modifier.fillMaxSize()) {
        LottieAnimation(composition = composition, modifier = Modifier.size(400.dp))
        LaunchedEffect(Unit) {
            delay(7000)

            if (FirebaseAuth.getInstance().currentUser?.email.isNullOrEmpty()){
            navController.navigate(ReaderScreen.LoginScreen.name)
        }else{
            navController.navigate(ReaderScreen.ReaderHomeScreen.name)
        }
        }

        Column(modifier = Modifier.padding(1.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom) {
            ReaderLogo()

            Spacer(modifier = Modifier.height(2.dp))
            Text(text = "\"Read And  Change Yourself \"",
                style = MaterialTheme.typography.headlineSmall,
                color = Color.LightGray)

        }

    }



}

@Composable
fun ReaderLogo(modifier: Modifier = Modifier) {
    Text(text = "Lazy Reader",
        modifier = modifier.padding(bottom = 16.dp),
        style = MaterialTheme.typography.headlineSmall,
        color = Color.Green.copy(alpha = 0.7f)
    )

}

