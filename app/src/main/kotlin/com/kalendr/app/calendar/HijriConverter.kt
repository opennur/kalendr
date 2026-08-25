package com.kalendr.app.calendar

import java.time.LocalDate
import kotlin.math.ceil

object HijriConverter {
    private const val ISLAMIC_EPOCH = 1_948_440

    fun convert(date: LocalDate): HijriDate {
        val julianDay = gregorianJulianDay(date)
        val year = ((30L * (julianDay - ISLAMIC_EPOCH) + 10_646) / 10_631).toInt()
        val month = minOf(
            12,
            ceil((julianDay - 29 - islamicJulianDay(year, 1)) / 29.5).toInt() + 1
        )
        val day = julianDay - islamicJulianDay(year, month) + 1
        return HijriDate(year, month, day.toInt())
    }

    private fun gregorianJulianDay(date: LocalDate): Long {
        val shift = (14 - date.monthValue) / 12
        val year = date.year + 4800 - shift
        val month = date.monthValue + 12 * shift - 3
        return (date.dayOfMonth + (153 * month + 2) / 5 + 365 * year +
            year / 4 - year / 100 + year / 400 - 32045).toLong()
    }

    private fun islamicJulianDay(year: Int, month: Int): Long =
        (29.5 * (month - 1)).toLong() + (year - 1) * 354L +
            ((3 + 11 * year) / 30) + ISLAMIC_EPOCH
}
