package com.kalendr.app.calendar

import java.time.LocalDate
import java.time.temporal.ChronoUnit

object JavaneseConverter {
    private val currentCycleAnchor = LocalDate.of(2026, 6, 17)
    private const val currentCycleYear = 1960
    private val pasaran = listOf("Legi", "Pahing", "Pon", "Wage", "Kliwon")
    private val monthLengths = intArrayOf(30, 29, 30, 29, 30, 29, 30, 29, 30, 29, 30, 29)

    fun convert(date: LocalDate): JavaneseDate {
        val days = ChronoUnit.DAYS.between(currentCycleAnchor, date)
        var remaining = days
        var year = currentCycleYear
        while (remaining < 0L) {
            year -= 1
            remaining += yearLength(year).toLong()
        }
        while (remaining >= yearLength(year).toLong()) {
            remaining -= yearLength(year).toLong()
            year += 1
        }
        var monthIndex = 0
        while (remaining >= monthLength(year, monthIndex).toLong()) {
            remaining -= monthLength(year, monthIndex).toLong()
            monthIndex += 1
        }
        val pasaranIndex = Math.floorMod(days + 4L, pasaran.size.toLong()).toInt()
        return JavaneseDate(year, JavaneseMonth.entries[monthIndex], (remaining + 1L).toInt(), pasaran[pasaranIndex])
    }

    private fun yearLength(year: Int): Int {
        val winduYear = Math.floorMod(year - 1555, 8) + 1
        return if (winduYear in setOf(2, 5, 8)) 355 else 354
    }

    private fun monthLength(year: Int, monthIndex: Int): Int =
        if (monthIndex == monthLengths.lastIndex && yearLength(year) == 355) 30 else monthLengths[monthIndex]
}
