package com.shall_we.login.data

import com.google.gson.annotations.SerializedName

data class Auth(
    @SerializedName("providerId") var providerId: String?,
    @SerializedName("provider") var provider: String,
    @SerializedName("email") var email: String?,
    @SerializedName("refreshToken") var refreshToken: String?
)