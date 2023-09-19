package com.shall_we.login.signup

data class UserData(
    val phoneNumber: String,
    val marketingConsent: Boolean,
    val age: Int,
    val gender: String
)