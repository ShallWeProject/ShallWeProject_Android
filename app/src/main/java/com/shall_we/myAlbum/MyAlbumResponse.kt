package com.shall_we.myAlbum

import com.google.gson.annotations.SerializedName

data class MyAlbumResponse(
    @SerializedName("data")
    val data: ArrayList<MyAlbumData>,
    @SerializedName("transaction_time")
    val transactionTime: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("description")
    val description: String?,
    @SerializedName("statusCode")
    val statusCode: Int
)