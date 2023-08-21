package com.shall_we.dto

import java.time.LocalDateTime


data class MemoryPhoto (
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val status: MemoryPhotoStatus,
    val id: Long,
    val memoryImgUrl: String,
    val reservation: Reservation
)

enum class MemoryPhotoStatus {
    ACTIVE,
    DELETE
}
