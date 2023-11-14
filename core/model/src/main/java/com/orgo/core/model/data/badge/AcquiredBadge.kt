package com.orgo.core.model.data.badge

data class AcquiredBadge(
    override val id: Int,
    override val objective: String,
    val acquiredTime: String,
    val description: String
) : Badge()