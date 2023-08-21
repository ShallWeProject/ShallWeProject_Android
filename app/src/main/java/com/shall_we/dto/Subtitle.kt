package com.shall_we.dto

import java.util.Date

data class Subtitle(
    val createdAt: Date,
    val updatedAt: Date,
    val status: Status,
    val subtitleId: Long,
    val title: String
) {
    enum class Status {
        ACTIVE,
        DELETE
    }
}

