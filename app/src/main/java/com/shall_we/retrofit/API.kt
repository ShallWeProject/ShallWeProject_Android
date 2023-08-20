package com.shall_we.retrofit

object API {
    const val BASE_URL : String = "http://43.201.110.47/"

    const val EXPERIENCE_GIFT_SEARCH = "api/v1/experience/gift/search"

    const val EXPERIENCE_GIFT_STTCATEGORY = "api/v1/experience/gift/stt-category/{SttCategoryId}"

    const val EXPERIENCE_GIFT_EXPCATEGORY = "api/v1/experience/gift/exp-category/{ExpCategoryId}"

    const val EXPERIENCE_GIFT="api/v1/experience/gift"

    const val EXPERIENCE_GIFT_EXPERIENCE_ID="api/v1/experience/gift/details/{ExperienceGiftId}"

    const val EXPERIENCE_GIFT_POPULAR = "api/v1/experience/gift/popular"

    const val USERS="/api/v1/users"

}

enum class RESPONSE_STATE {
    OKAY,
    FAIL
}