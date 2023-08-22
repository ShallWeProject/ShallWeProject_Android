package com.shall_we.dto


import java.util.Date

data class User(
    val createdAt: Date,
    val updatedAt: Date,
    val status: Status,
    val id: Long,
    val name: String,
    val birthDay: String,
    val age: Int,
    val phoneNumber: String,
    val email: String,
    val password: String,
    val profileImgUrl: String,
    val gender: Gender,
    val marketingConsent: Boolean,
    val provider: Provider,
    val providerId: String,
    val role: Role
) {
    enum class Status {
        ACTIVE,
        DELETE
    }

    enum class Gender {
        MALE,
        FEMALE,
        UNKNOWN
    }

    enum class Provider {
        LOCAL,
        FACEBOOK,
        GOOGLE,
        GITHUB,
        KAKAO,
        NAVER
    }

    enum class Role {
        ADMIN,
        USER
    }
}
