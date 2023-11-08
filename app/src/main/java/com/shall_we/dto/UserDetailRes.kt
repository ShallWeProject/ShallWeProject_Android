package com.shall_we.dto

import com.google.gson.annotations.SerializedName
import com.shall_we.login.data.AuthTokenData

data class UserDetailRes (
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("birthday")
    val birthDay: String?,
    @SerializedName("age")
    val age: Int?,
    @SerializedName("phoneNumber")
    val phoneNumber: String?,
    @SerializedName("email")
    val email: String,
    @SerializedName("profileImgUrl")
    val profileImgUrl: String,
    @SerializedName("gender")
    val gender: String?
)

data class UserDetail(
    @SerializedName("data")
    val data: UserDetailRes,
    @SerializedName("transaction_time")
    val transactionTime: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("description")
    val description: String?,
    @SerializedName("statusCode")
    val statusCode: Int
)