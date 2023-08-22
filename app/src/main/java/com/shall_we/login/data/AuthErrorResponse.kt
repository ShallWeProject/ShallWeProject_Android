package com.shall_we.login.data

import com.google.gson.annotations.SerializedName

data class AuthErrorResponse(
    @SerializedName("check")
    val check: Boolean,
    @SerializedName("information")
    val information: ErrorInformation
) {
    data class ErrorInformation(
        @SerializedName("timestamp")
        val timestamp: String,
        @SerializedName("message")
        val message: String,
        @SerializedName("code")
        val code: Int?,
        @SerializedName("status")
        val status: Int,
        @SerializedName("errorClass")
        val errorClass: String?,
        @SerializedName("errors")
        val errors: List<String>
    )
}