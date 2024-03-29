package com.shall_we.dto

import com.google.gson.annotations.SerializedName

data class SttCategoryData(
    @SerializedName("data")
    val data: ArrayList<MainSttCategoryRes>,
    @SerializedName("transaction_time")
    val transactionTime: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("description")
    val description: String?,
    @SerializedName("statusCode")
    val statusCode: Int
)

data class MainSttCategoryRes(
    @SerializedName("subtitleTitle")
    val subtitle: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("price")
    val price: Int,
    @SerializedName("giftImgUrl")
    val giftImgUrl: ArrayList<String>,
    @SerializedName("experienceGiftId")
    val experienceGiftId: Int
)
