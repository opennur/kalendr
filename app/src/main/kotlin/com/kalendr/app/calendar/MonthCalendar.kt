package com.kalendr.app.calendar

import java.time.LocalDate
import java.time.YearMonth

data class CalendarCell(val date: LocalDate?) {
    fun isToday(today: LocalDate): Boolean = date == today
}

data class CalendarMonth(val month: YearMonth, val cells: List<CalendarCell>)

object MonthCalendar {
    fun build(month: YearMonth): CalendarMonth {
        val leadingBlanks = month.atDay(1).dayOfWeek.value % 7
        val cells = (0 until 42).map { index ->
            val day = index - leadingBlanks + 1
            CalendarCell(day.takeIf { it in 1..month.lengthOfMonth() }?.let(month::atDay))
        }
        return CalendarMonth(month, cells)
    }
}
