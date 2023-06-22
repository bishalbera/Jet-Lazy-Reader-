package com.bishal.lazyreader.presentation.screens.login

import com.bishal.lazyreader.domain.model.MUser

data class SignInResult(
    val data: MUser?,
    val errorMessage: String?
)
