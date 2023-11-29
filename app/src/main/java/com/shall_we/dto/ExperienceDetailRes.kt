package com.shall_we.dto

import com.google.gson.annotations.SerializedName

data class ExperienceDetailRes(
    val giftImageUrl: List<String>? = null,
    val title: String? = null,
    val subtitle: String? = null,
    val price: Int? = null,
    val explanation: List<ExplanationRes>? = null,
    val description: String? = null,
    @SerializedName("expCategory")
    val experienceCategory: String? = null,
    @SerializedName("sttCategory")
    val statementCategory: String? = null,
    @SerializedName("experienceGiftId")
    val experienceGiftId: Int? = null
)

data class ExplanationRes(
    val description: String? = null,
    val explanationUrl: String? = null
)
