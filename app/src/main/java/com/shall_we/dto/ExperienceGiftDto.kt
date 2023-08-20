package com.shall_we.dto

class ExperienceGiftDto(
    var thumbnail: String? = null,
    var sttCategory: String? = null,
    var price: Int? = null,
    var giftImgUrl: String? = null,
    var experienceGiftId: Int? = null,
    var subtitle: String? = null,
    var description: String? = null,
    var title: String? = null,
    var expCategory: String? = null,
    var formattedPrice: String? = null
) {
    override fun toString(): String {
        return "ClassPojo [thumbnail = $thumbnail, sttCategory = $sttCategory, price = $price, giftImgUrl = $giftImgUrl, experienceGiftId = $experienceGiftId, subtitle = $subtitle, description = $description, title = $title, expCategory = $expCategory, formattedPrice = $formattedPrice]"
    }
}
