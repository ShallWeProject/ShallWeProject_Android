package com.shall_we.login.data

import com.google.gson.annotations.SerializedName

data class AuthSignOutResponse(
    @SerializedName("message") val message: String
)