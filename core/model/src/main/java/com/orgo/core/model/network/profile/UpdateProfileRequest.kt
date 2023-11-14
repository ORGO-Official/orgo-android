package com.orgo.core.model.network.profile

import android.graphics.Bitmap

data class UpdateProfileRequest(
    val nickname : String,
    val profileImage : Bitmap
)

data class UpdateNicknameRequest(
    val nickname: String
)