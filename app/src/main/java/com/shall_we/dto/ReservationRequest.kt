package com.shall_we.dto


data class ReservationRequest(
    val experienceGiftId: Int, // integer($int64)으로 변경
    val persons: Int, // integer($int64)으로 변경
    val date: String?,
    val time: String?, // LocalTime 객체 추가
    val phoneNumber: String,
    val imageKey: String,
    val invitationComment: String
)

