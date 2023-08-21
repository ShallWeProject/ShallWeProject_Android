package com.shall_we.dto

import java.util.Date

data class ExperienceGift(
    val createdAt: String,
    val updatedAt: String,
    val status: Status,
    val experienceGiftId: Int,
    val title: String,
    val subtitle: Subtitle,
    val thumbnail: String,
    val price: Int,
    val expCategory: ExpCategory,
    val sttCategory: SttCategory,
    val description: String,
    val giftImgKey: String
) {
    enum class Status {
        ACTIVE,
        DELETE
    }
}

