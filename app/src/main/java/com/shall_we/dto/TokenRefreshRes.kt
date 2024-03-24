package com.shall_we.dto

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.shall_we.login.data.AuthTokenData
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TokenRefreshRes(
    @SerializedName("data")
    val data: AuthTokenData,
    @SerializedName("transaction_time")
    val transactionTime: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("description")
    val description: String?,
    @SerializedName("statusCode")
    val statusCode: Int
): Parcelable

@Parcelize
data class AuthTokenData(
    @SerializedName("accessToken")
    val accessToken: String,
    @SerializedName("refreshToken")
    val refreshToken: String,
    @SerializedName("tokenType")
    val tokenType: String
) : Parcelable