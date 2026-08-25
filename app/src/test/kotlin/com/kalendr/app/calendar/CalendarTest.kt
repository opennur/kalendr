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
    fun `date picker selection round trips February 1900 in UTC`() {
        val selected = LocalDate.of(1900, 2, 14)

        assertEquals(selected, DateSelection.fromUtcMillis(DateSelection.toUtcMillis(selected)))
        assertEquals(YearMonth.of(1900, 2), MonthCalendar.build(YearMonth.from(selected)).month)
        assertEquals(28, MonthCalendar.build(YearMonth.of(1900, 2)).cells.count { it.date != null })
    }

    @Test
    fun `calendar cell identifies today`() {
        val today = LocalDate.of(2026, 8, 25)
        val month = MonthCalendar.build(YearMonth.from(today))

        assertTrue(month.cells.any { it.isToday(today) })
    }

    @Test
    fun `holiday API parser identifies national holiday`() {
        val holidays = IndonesianHolidays.parse(
            """
            {"success":true,"data":[{"date":"2026-08-25","name":"Maulid Nabi Muhammad SAW","type":"holiday"}]}
            """.trimIndent(),
        )

        assertEquals(1, holidays.size)
        assertEquals("Maulid Nabi Muhammad SAW", holidays.single().name)
        assertEquals(IndonesianDayType.NATIONAL_HOLIDAY, holidays.single().type)
    }

    @Test
    fun `holiday API parser keeps joint leave separate from national holiday`() {
        val holidays = IndonesianHolidays.parse(
            """
            {"success":true,"data":[{"date":"2026-12-24","name":"Kelahiran Yesus Kristus","type":"leave"}]}
            """.trimIndent(),
        )

        assertEquals(1, holidays.size)
        assertEquals(IndonesianDayType.JOINT_LEAVE, holidays.single().type)
    }

    @Test
    fun `Nager parser preserves predictable future national holidays`() {
        val holidays = IndonesianHolidays.parseNager(
            """
            [{"date":"2027-01-01","localName":"Tahun Baru Masehi","types":["Public"]}]
            """.trimIndent(),
            YearMonth.of(2027, 1),
        )

        assertEquals(1, holidays.size)
        assertEquals(LocalDate.of(2027, 1, 1), holidays.single().date)
        assertEquals(IndonesianDayType.NATIONAL_HOLIDAY, holidays.single().type)
    }
}
