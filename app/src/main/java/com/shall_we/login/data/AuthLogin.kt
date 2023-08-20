package com.shall_we.login.data

import com.google.gson.annotations.SerializedName

data class AuthLogin(
    @SerializedName("providerId") var providerId: String,
    @SerializedName("email") var email: String
)