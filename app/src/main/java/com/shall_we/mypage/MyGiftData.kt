package com.shall_we.mypage

data class MyGiftData(
    val idx: Int,
    val title: String,
    val description: String,
    val date: String,
    val time: String,
    val cancellation: Boolean,
    val messageImgUrl: Int,
    val message: String
    )