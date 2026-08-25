package com.kalendr.app.calendar

import java.time.LocalDate
import java.time.YearMonth
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CalendarTest {
    @Test
    fun `known Gregorian date maps to civil Hijri`() {
        val result = HijriConverter.convert(LocalDate.of(2024, 3, 11))

        assertEquals(1445, result.year)
        assertEquals(9, result.month)
        assertEquals(1, result.day)
    }

    @Test
    fun `Gregorian day keeps five day pasaran cycle`() {
        val result = JavaneseConverter.convert(LocalDate.of(2024, 3, 11))

        assertEquals("Pahing", result.pasaran)
        assertTrue(result.day in 1..30)
        assertTrue(result.month in JavaneseMonth.entries)
    }

    @Test
    fun `today vector maps to Mulud 1960 and Wage`() {
        val result = JavaneseConverter.convert(LocalDate.of(2026, 8, 25))

        assertEquals(1960, result.year)
        assertEquals(JavaneseMonth.MULUD, result.month)
        assertEquals(11, result.day)
        assertEquals("Wage", result.pasaran)
    }

    @Test
    fun `conversion remains valid well before current anchor`() {
        val result = JavaneseConverter.convert(LocalDate.of(1900, 1, 1))

        assertTrue(result.year in 1500..1960)
        assertTrue(result.day in 1..30)
        assertTrue(result.pasaran in listOf("Legi", "Pahing", "Pon", "Wage", "Kliwon"))
    }

    @Test
    fun `formatting exposes both calendar policies`() {
        val result = CalendarFormatter.format(LocalDate.of(2024, 3, 11))

        assertTrue(result.hijri.contains("Islamic Civil"))
        assertTrue(result.javanese.contains("Weton"))
    }

    @Test
    fun `monthly grid places first day under the correct weekday`() {
        val month = MonthCalendar.build(YearMonth.of(2024, 3))

        assertEquals(42, month.cells.size)
        assertEquals(null, month.cells[0].date)
        assertEquals(LocalDate.of(2024, 3, 1), month.cells[5].date)
        assertEquals(LocalDate.of(2024, 3, 31), month.cells[35].date)
        assertEquals(null, month.cells[36].date)
    }

    @Test
    fun `calendar cell identifies today`() {
        val today = LocalDate.of(2026, 8, 25)
        val month = MonthCalendar.build(YearMonth.from(today))

        assertTrue(month.cells.any { it.isToday(today) })
    }
}
