package com.shall_we.retrofit

object API {
    const val BASE_URL : String = "http://43.201.110.47/"

    const val EXPERIENCE_GIFT_SEARCH = "api/v1/experience/gift/search"

    const val EXPERIENCE_GIFT_STTCATEGORY = "api/v1/experience/gift/stt-category/{SttCategoryId}"

    const val EXPERIENCE_GIFT_EXPCATEGORY = "api/v1/experience/gift/exp-category/{ExpCategoryId}"

    const val AUTH_SIGN_UP = "auth/sign-in"

    const val AUTH_SIGN_IN = "auth/sign-up"

    const val AUTH_SIGN_OUT = "auth/sign-out"

    const val AUTH_REFRESH = "auth/refresh"

    const val SEND_ONE = "auth/send-one"

    const val VALID_VERIFICATION = "auth/valid-verification-code"
    const val EXPERIENCE_GIFT="api/v1/experience/gift"

    const val EXPERIENCE_GIFT_EXPERIENCE_ID="api/v1/experience/gift/details/{ExperienceGiftId}"

    const val EXPERIENCE_GIFT_POPULAR = "api/v1/experience/gift/popular"

}

enum class RESPONSE_STATE {
    OKAY,
    FAIL
}