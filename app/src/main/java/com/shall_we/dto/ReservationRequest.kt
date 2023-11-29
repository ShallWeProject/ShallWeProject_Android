package com.shall_we.dto

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.Date


data class ReservationRequest(
    val experienceGiftId: Int, // integer($int64)으로 변경
    val persons: Int, // integer($int64)으로 변경
    val date: String,
    val time: com.shall_we.dto.LocalTime, // LocalTime 객체 추가
    val phoneNumber: String,
    val imageKey: String,
    val invitationComment: String
)

