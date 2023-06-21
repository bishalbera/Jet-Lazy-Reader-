package com.bishal.lazyreader.screens.login


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bishal.lazyreader.model.MUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch



class LoginScreenViewModel :ViewModel() {

    private val auth: FirebaseAuth = Firebase.auth


    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading



    private val _state = MutableStateFlow(SignInState())
    val state = _state.asStateFlow()


    


    fun onSignInResult(result: SignInResult) {
        _state.update { it.copy(
            isSignInSuccessful = result.data != null,
            signInError = result.errorMessage
        ) }
    }

    fun resetState() {
        _state.update { SignInState() }
    }



    fun signInWithEmailAndPassword(email: String, password: String, home: () -> Unit) =
        viewModelScope.launch {
            try {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d(
                                "FB",
                                "signInWithEmailAndPassword: Yayayay! ${task.result.toString()}"
                            )
                            //TODO:take them home screen
                            home()
                        } else {
                            Log.d("FB", "signinwithemailandpassword: ${task.result.toString()}")
                        }

                    }

            } catch (ex: Exception) {
                Log.d("FB", "signinwithemailandpassword: ${ex.message}")
            }

        }



    fun createUserWithEmailAndPassword(
        email: String,
        password: String,
        home: () -> Unit
    ) {
        if (_loading.value == false) {
            _loading.value = true
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val displayName = task.result?.user?.email?.split('@')?.get(0)
                        createUser(displayName)
                        home()
                    } else {
                        Log.d("FB", "createuserwithemailandpassword: ${task.result.toString()}")
                    }
                    _loading.value = false
                }
        }


    }

    private fun createUser(displayName: String?) {
        val userId = auth.currentUser?.uid

        val user = MUser(userId = userId.toString(),
            displayName = displayName.toString(),
            avatarUrl = "",
            quote = "Enjoy every instant of your life",
            profession = "Android Developer",
            id = null).toMap()

        FirebaseFirestore.getInstance().collection("users")
            .add(user)


    }

     fun createUserWithGoogle(result: SignInResult){
        val userId = result.data?.userId
        val displayName = result.data?.displayName

        val user = MUser(
            userId = userId.toString(),
            displayName = displayName.toString(),
            avatarUrl = result.data?.avatarUrl.toString(),
            quote = "Enjoy every instant of your life",
            profession = "Android Developer",
            id = null
        ).toMap()

        FirebaseFirestore.getInstance().collection("users")
            .add(user)
    }
}