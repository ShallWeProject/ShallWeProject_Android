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

    const val MEMORY_PHOTO = "/api/v1/memory-photo/{date}"

    const val USERS_GIFT_SEND = "/api/v1/users/gift/send"

    const val USERS_GIFT_RECEIVE = "/api/v1/users/gift/receive"

    const val DELETE_RESERVATION = "/api/v1/reservations"
    const val EXPERIENCE_GIFT_POPULAR = "api/v1/experience/gift/popular"

    const val USERS="/api/v1/users"

    //예약정보불러오기,예약수정
    const val RESERVATIONS="api/v1/reservations/user"
    //const val RESERVATIONS_USER="api/v1/reservations/user"
    //해당 경험 선물에 생성된 예약 조회
    const val RESERVATIONS_GIFT="api/v1/reservations/giftId"

    const val UPLOAD_IMG=""
}

enum class RESPONSE_STATE {
    OKAY,
    FAIL
}