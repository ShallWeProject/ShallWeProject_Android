package com.shall_we.dto

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.time.LocalDateTime
import java.util.Date


data class ReservationRequest(
    val experienceGiftId: Int,
    val persons: Int,
    val date: String,
    val receiverName: String,
    val phoneNumber: String,
    val imageKey: String,
    val invitationComment: String,
    val reservationStatus: String
)
