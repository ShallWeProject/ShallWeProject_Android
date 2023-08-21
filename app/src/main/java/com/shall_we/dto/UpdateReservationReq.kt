package com.shall_we.dto

data class UpdateReservationReq (
    val id: Long,
    val persons: Int,
    val date: String,
    val sender: User,
    val receiver: User,
    val phone_number: String,
    val invitation_img: String,
    val invitation_comment: String
)

