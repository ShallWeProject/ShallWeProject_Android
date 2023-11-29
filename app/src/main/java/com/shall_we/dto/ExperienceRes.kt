package com.shall_we.dto

import com.google.gson.annotations.SerializedName
import com.shall_we.login.data.AuthTokenData

data class PopularRes(
    @SerializedName("data")
    val data: ArrayList<ExperienceRes>,
    @SerializedName("transaction_time")
    val transactionTime: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("description")
    val description: String?,
    @SerializedName("statusCode")
    val statusCode: Int
)

data class ExperienceRes(
    @SerializedName("thumbnail")
    val thumbnail: String?,
    @SerializedName("subtitle")
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
