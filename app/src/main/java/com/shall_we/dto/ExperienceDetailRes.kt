package com.shall_we.dto

import com.google.gson.annotations.SerializedName

data class ExperienceDetailRes(
    val data: GiftDTO,
    val transaction_time: String,
    val status: String,
    val description: String?,
    val statusCode: Int
)
data class GiftDTO(
    val giftImgUrl: List<String>,
    val title: String,
    val subtitle: String,
    val price: Int,
    val explanation: List<Explanation>,
    val description: String,
    val expCategory: String,
    val sttCategory: String,
    val experienceGiftId: Int
)

data class Explanation(
    val stage: String,
    val description: String,
    val explanationUrl: String
)
