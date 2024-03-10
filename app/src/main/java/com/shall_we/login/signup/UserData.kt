package com.shall_we.login.signup

data class UserData(
    val name : String,
    val phoneNumber: String,
    val marketingConsent: Boolean,
    val age: Int,
    val gender: String
)