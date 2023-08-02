package com.shall_we.mypage

import java.time.LocalDate
import java.time.LocalTime

data class MyGiftDto(
    val MyGift_idx: Int,
    val MyGift_title: String,
    val MyGift_description: String,
    val MyGift_date: String,
    val MyGift_time: String,
    val MyGift_cancellation: Boolean,
    val MyGift_messageImgUrl: String,
    val MyGift_message: String
    )
