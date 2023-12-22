package com.shall_we.dto

import com.google.gson.annotations.SerializedName
import java.util.Date

data class UpdateReservationReq (
    val reservationId: Int,
    val date: String,
    val time: String
)
