package com.orgo.core.model.data

sealed class UserAuthState {

    data class Authenticated(
        val userToken: UserToken,
        val loginSocialType: SocialType,
        val message: String = "",
    ) : UserAuthState()

    object Unauthenticated : UserAuthState()

    object LoggedOut : UserAuthState()

    object TokenExpired : UserAuthState()

}
