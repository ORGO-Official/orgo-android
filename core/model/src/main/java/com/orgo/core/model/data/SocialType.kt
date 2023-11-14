package com.orgo.core.model.data

enum class SocialType(val value : String){
    NAVER("naver"),
    KAKAO("kakao"),
    GOOGLE("google")
}

fun mapSocialType(value: String): SocialType {
    return when (value) {
        "naver" -> SocialType.NAVER
        "kakao" -> SocialType.KAKAO
        "google" -> SocialType.GOOGLE
        else -> throw IllegalArgumentException("Invalid social type value: $value")
    }
}