package com.shall_we.dto

import java.util.Date

data class ExpCategory(
    val createdAt: String,
    val updatedAt:String,
    val status: Status,
    val expCategoryId: Int,
    val expCategory: String,
    val imageKey: String
) {
    enum class Status {
        ACTIVE,
        DELETE
    }
}
