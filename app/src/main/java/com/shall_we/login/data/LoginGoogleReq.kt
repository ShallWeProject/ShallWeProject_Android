package com.shall_we.login.data

import com.google.gson.annotations.SerializedName

data class LoginGoogleReq (
    @SerializedName("grant_type")
    private val grant_type: String,
    @SerializedName("client_id")
    private val client_id: String,
    @SerializedName("client_secret")
    private val client_secret: String,
    @SerializedName("redirect_uri")
    private val redirect_uri: String,
    @SerializedName("code")
    private val code: String
)