package com.shall_we.signup

data class UserData(
    var phoneNumber: String,
    val marketingConsent: Boolean,
    val age: Int,
    val gender: String // MALE / FEMALE / UNKNOWN
)