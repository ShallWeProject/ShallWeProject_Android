package com.shall_we.dto
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LocalTime(
    val hour: Int,   // integer($int32)
    val minute: Int, // integer($int32)
    val second: Int, // integer($int32)
    val nano: Int    // integer($int32)
) : Parcelable

