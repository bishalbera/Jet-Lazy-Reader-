package com.bishal.lazyreader.screens.login

import com.bishal.lazyreader.model.MUser

data class SignInResult(
    val data: MUser?,
    val errorMessage: String?
)
