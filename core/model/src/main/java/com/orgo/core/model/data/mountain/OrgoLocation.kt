package com.orgo.core.model.data.mountain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class OrgoLocation(
    val altitude: Double,
    val latitude: Double,
    val longitude: Double
):Parcelable