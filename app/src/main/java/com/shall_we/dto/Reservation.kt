package com.shall_we.dto

import java.time.LocalDateTime

data class Reservation (
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val status: ReservationStatus,
    val id: Long,
    val experienceGift: ExperienceGift,
    val sender: User,
    val persons: Int,
    val date: LocalDateTime,
    val receiver: User,
    val phoneNumber: String,
    val invitationImg: String,
    val invitationComment: String,
    val reservationStatus: ReservationStatus,
    val memoryPhotos: List<MemoryPhoto>
)

enum class ReservationStatus {
    CANCELLED,
    BOOKED,
    COMPLETED
}
