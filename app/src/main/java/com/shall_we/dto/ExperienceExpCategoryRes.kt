package com.shall_we.dto

import com.google.gson.annotations.SerializedName

data class catergoryResponse(
    @SerializedName("data")
    val data: ArrayList<ExperienceExpCategoryRes>,
    @SerializedName("transaction_time")
    val transactionTime: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("description")
    val description: String?,
    @SerializedName("statusCode")
    val statusCode: Int
)

data class ExperienceExpCategoryRes(
    @SerializedName("expCategoryId")
    val expCategoryId: Int,
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
