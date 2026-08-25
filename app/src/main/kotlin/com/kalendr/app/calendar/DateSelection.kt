package com.kalendr.app.calendar

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset

object DateSelection {
    fun toUtcMillis(date: LocalDate): Long =
        date.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()

    fun fromUtcMillis(millis: Long): LocalDate =
        Instant.ofEpochMilli(millis).atZone(ZoneOffset.UTC).toLocalDate()
}
