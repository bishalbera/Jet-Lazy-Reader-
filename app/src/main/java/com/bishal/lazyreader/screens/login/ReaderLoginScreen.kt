@file:OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)

package com.bishal.lazyreader.screens.login

import android.app.Activity.RESULT_OK
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.bishal.lazyreader.R
import com.bishal.lazyreader.components.EmailInput
import com.bishal.lazyreader.components.PasswordInput
import com.bishal.lazyreader.navigation.ReaderScreen
import com.google.android.gms.auth.api.identity.Identity
import kotlinx.coroutines.launch



@Composable
fun ReaderLoginScreen(
    navController: NavController,
    viewModel: LoginScreenViewModel = hiltViewModel(),
){

    val showLoginForm = rememberSaveable { mutableStateOf(true) }

    val background = painterResource(id = R.drawable.background)

Surface(
    modifier = Modifier
        .fillMaxSize()
        .background(
            Brush.verticalGradient(
                listOf(Color.Transparent, Color.Black),
                startY = 0f,
                endY = 800f
            )
        )
) {
    Image(
        painter = background,
        contentDescription = "background image",
        contentScale = ContentScale.FillBounds
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {



        if (showLoginForm.value) UserForm(loading = false, isCreateAccount = false, navController = navController){email, password ->
            //TODO: create FB login
            viewModel.signInWithEmailAndPassword( email, password){
                navController.navigate(ReaderScreen.ReaderHomeScreen.name)
            }
        }
        else{
            UserForm(loading = false, isCreateAccount = true, navController = navController){ email, password ->
                viewModel.createUserWithEmailAndPassword(email, password) {
                    navController.navigate(ReaderScreen.ReaderHomeScreen.name)
                }
            }
        }

    }
    Spacer(modifier = Modifier.height(10.dp))
    Row (
        modifier = Modifier.padding(15.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.Bottom
    ){
        val text = if (showLoginForm.value) "Sign up" else "Login"
        Text(text = "New User?")
        Text(text = text,
            modifier = Modifier
                .clickable {
                    showLoginForm.value = !showLoginForm.value
                }
                .padding(start = 5.dp),
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.tertiary)

    }

}
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun UserForm(
    loading: Boolean = false,
    isCreateAccount: Boolean = false,
    viewModel: LoginScreenViewModel = hiltViewModel(),
    navController: NavController,
    onDone: (String, String) -> Unit = {email, pwd ->}

){
    val email = rememberSaveable { mutableStateOf("") }
    val password = rememberSaveable { mutableStateOf("") }
    val passwordVisibility = rememberSaveable { mutableStateOf(false) }
    val passwordFocusRequest = FocusRequester.Default
    val keyboardController = LocalSoftwareKeyboardController.current
    val valid  = remember(email.value, password.value){
        email.value.trim().isNotEmpty() && password.value.trim().isNotEmpty()

    }

    val modifier = Modifier
        .height(250.dp)
        .background(Color.Transparent)
        .verticalScroll(rememberScrollState())
    Alignment.CenterHorizontally
    
    Column(modifier,
    horizontalAlignment = Alignment.CenterHorizontally) {
        if(isCreateAccount) Text(text = stringResource(id = R.string.create_acct),
            modifier = Modifier.padding(4.dp)) else Text(text = "")
        EmailInput(emailState = email, enabled = !loading, onAction = KeyboardActions{
            passwordFocusRequest.requestFocus()
        })

        PasswordInput(
            modifier = Modifier,
            passwordState = password,
            labelId = "Password",
            enabled = !loading,
            passwordVisibility = passwordVisibility,
            onAction = KeyboardActions{
                if (!valid) return@KeyboardActions
                onDone(email.value.trim(), password.value.trim())
            })

        SubmitButton(
            textID = if (isCreateAccount) "Create Account" else "Login",
            loading = loading,
            validInputs = valid,
        ){
            onDone(email.value.trim(), password.value.trim())

            keyboardController?.hide()
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        val coroutineScope = rememberCoroutineScope()
        val context = LocalContext.current
        val state by viewModel.state.collectAsState()

        val googleAuthUiClient by lazy {
            GoogleAuthUiClient(
                context = context,
                oneTapClient = Identity.getSignInClient(context)
            )
        }


        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartIntentSenderForResult()

        ) { result ->
           if (result.resultCode == RESULT_OK){
               coroutineScope.launch {
                   val signInResult = googleAuthUiClient.signInWithIntent(
                       intent = result.data ?: return@launch
                   )
                   viewModel.onSignInResult(signInResult)

                   viewModel.createUserWithGoogle(result = signInResult)
               }
           }

        }
        LaunchedEffect(key1 = state.isSignInSuccessful){
            if (state.isSignInSuccessful){

                navController.navigate(ReaderScreen.ReaderHomeScreen.name)
                viewModel.resetState()
            }

        }

        SignInWithGoogleBtn( onSignInClick = {

            coroutineScope.launch {
                val signInIntentSender = googleAuthUiClient.signIn()
                launcher.launch (
                    IntentSenderRequest.Builder(
                        signInIntentSender ?: return@launch
                    ).build()
                )
            }

        })




        

    }


}

@Composable
fun SubmitButton(textID: String,
                 loading: Boolean,
                 validInputs: Boolean,
                 onClick: () -> Unit) {
    Button(onClick =  onClick,
           modifier = Modifier
               .padding(3.dp)
               .fillMaxWidth(),
            enabled = !loading && validInputs,
            shape = CircleShape) {
        if (loading) CircularProgressIndicator(modifier = Modifier.size(25.dp))
        else Text(
            text = textID,
            color = Color(0xff45271D),
            modifier = Modifier.padding(5.dp)
        )


    }

}

@Composable
fun SignInWithGoogleBtn(

    onSignInClick: () -> Unit,

) {


    IconButton(onClick =  onSignInClick ) {
        Icon(
            painterResource(id = R.drawable.ic_google),
            contentDescription = "Google icon",
            modifier = Modifier.size(50.dp),
            tint = Color.Unspecified
        )
        
    }

}
