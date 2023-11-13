package com.orgo.core.model.data.user


data class UserProfile(
    val id : Long,
    val nickname: String,
    val email : String,
    val profileImage : String?,
    val loginType : String
){
    companion object {
        val DUMMY_PROFILE = UserProfile(
            id = 9,
            nickname = "테스트유저",
            email = "hansol8701@test.com",
            profileImage = "",
            loginType = "NAVER"
        )
    }
}
