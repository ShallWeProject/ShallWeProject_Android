package com.shall_we.dto

 data class ValidTimeRes(
    val data: List<ReservationItem>,
    val transactionTime: String,
    val status: String,
    val description: String?,
    val statusCode: Int
)

data class ReservationItem(
    val reservationId: Int,
    val status: String,
    val time: String
)