package com.shall_we.dto

import java.util.Date

data class SttCategory(
    val createdAt: String,
    val updatedAt: String,
    val status: Status,
    val sttCategoryId: Int,
    val sttCategory: String,
    val imageKey: String
) {
    enum class Status {
        ACTIVE,
        DELETE
    }
}