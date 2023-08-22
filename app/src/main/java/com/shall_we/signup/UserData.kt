package com.shall_we.signup

data class UserData(
    val phoneNumber: String,
    val marketingConsent: Boolean,
    val age: Int,
    val gender: String
)