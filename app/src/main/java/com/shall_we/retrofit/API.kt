package com.shall_we.retrofit

object API {
    const val BASE_URL : String = "http://43.201.110.47/"

    const val EXPERIENCE_GIFT_SEARCH = "api/v1/experience/gift/search"

    const val EXPERIENCE_GIFT_STTCATEGORY = "api/v1/experience/gift/stt-category/{SttCategoryId}"

    const val EXPERIENCE_GIFT_EXPCATEGORY = "api/v1/experience/gift/exp-category/{ExpCategoryId}"

    const val EXPERIENCE_GIFT="api/v1/experience/gift"

    const val EXPERIENCE_GIFT_EXPERIENCE_ID="api/v1/experience/gift/details/{ExperienceGiftId}"

    const val MEMORY_PHOTO = "/api/v1/memory-photo/{date}"

    const val USERS_GIFT_SEND = "/api/v1/users/gift/send"

    const val USERS_GIFT_RECEIVE = "/api/v1/users/gift/receive"

    const val DELETE_RESERVATION = "/api/v1/reservations"
}

enum class RESPONSE_STATE {
    OKAY,
    FAIL
}