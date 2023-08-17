package com.shall_we.dto

import android.icu.number.Precision.integer

data class ExperienceReq(

    val title:String,
    val thumbnail:String,
    val price:Int,
    val expCategoryId:Int,
    val sttCategory: Int,
    val subtitleId: Int,
    val description: String
)
