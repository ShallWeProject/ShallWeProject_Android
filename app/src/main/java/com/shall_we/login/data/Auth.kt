package com.shall_we.login.data

import com.google.gson.annotations.SerializedName

data class Auth(
    @SerializedName("providerId") var providerId: String,
    @SerializedName("provider") var provider: String,
    @SerializedName("nickname") var nickname: String,
    @SerializedName("email") var email: String,
    @SerializedName("profileImgUrl") var profileImgUrl: String
)