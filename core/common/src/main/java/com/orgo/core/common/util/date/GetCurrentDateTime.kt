package com.orgo.core.common.util.date

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun getCurrentDateTimeAsString(): String {
    val currentDateTime = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS")
    return currentDateTime.format(formatter)
}