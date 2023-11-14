package com.orgo.core.model.data.badge

data class UnacquiredBadge(
    override val id: Int,
    override val objective: String
) : Badge()