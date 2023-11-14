package com.orgo.core.common.util.date

import timber.log.Timber
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


fun convertDateFormat(input: String): String {
    Timber.tag("convertDateFormat").d("input : $input")

    val inputDateTime = LocalDateTime.parse(input, DateTimeFormatter.ISO_DATE_TIME)
    val returnDate = inputDateTime.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))

    Timber.tag("convertDateFormat").d("returnDate : $returnDate")
    return returnDate
}