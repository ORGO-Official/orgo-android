package com.orgo.core.model.data.badge

sealed class Badge {
    abstract val id: Int
    abstract val objective: String
}