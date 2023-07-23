package com.shall_we.mypage

import java.time.LocalDate
import java.time.LocalTime

data class MyGiftDto(
    val MyGift_idx: Int,
    val MyGift_title: String,
    val MyGift_description: String,
    val MyGift_date: LocalDate,
    val MyGift_time: LocalTime,
    val MyGift_cancellation: String,
    val MyGift_messageImgUrl: String,
    val MyGift_message: String
    )
