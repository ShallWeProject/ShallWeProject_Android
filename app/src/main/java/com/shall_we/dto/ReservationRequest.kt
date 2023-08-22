package com.shall_we.dto

data class ReservationRequest(
    val experienceGiftId: Int,
    val persons: Int,
    val date: String,
    val receiverName: String,
    val phoneNumber: String,
    val imageKey: String,
    val invitationComment: String,
    val reservationStatus: ReservationStatus
)
{
    enum class ReservationStatus {
        CANCELLED,
        BOOKED,
        COMPLETED
    }
}