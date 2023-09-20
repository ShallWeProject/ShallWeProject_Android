package com.shall_we.mypage

data class MyGiftData(
    val idx: Int,
    val title: String,
    val description: String,
    val date: String,
    val time: String,
    val cancellable: Boolean,
    val message: String,
    val person: String
    )