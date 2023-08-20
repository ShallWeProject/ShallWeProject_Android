package com.shall_we.login.data

import com.google.gson.annotations.SerializedName

data class ErrorResponse(
    @SerializedName("timestamp")
    val timestamp: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("code")
    val code: String,
    @SerializedName("status")
    val status: Int,
    @SerializedName("class")
    val errorResponse: String,
    @SerializedName("errors")
    val errors: List<CustomFieldError>
)
data class CustomFieldError(
    @SerializedName("field")
    val field: String,
    @SerializedName("value")
    val value: String,
    @SerializedName("reason")
    val reason: String
)