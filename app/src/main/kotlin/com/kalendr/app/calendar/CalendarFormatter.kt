package com.kalendr.app.calendar

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

object CalendarFormatter {
    private val gregorianFormatter = DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy", Locale("id", "ID"))
    private val hijriMonths = listOf(
        "Muharram", "Safar", "Rabiulawal", "Rabiulakhir", "Jumadilawal", "Jumadilakhir",
        "Rajab", "Syaban", "Ramadan", "Syawal", "Zulkaidah", "Zulhijah"
    )

    fun format(date: LocalDate): CalendarDisplay {
        val hijri = HijriConverter.convert(date)
        val java = JavaneseConverter.convert(date)
        return CalendarDisplay(
            gregorian = date.format(gregorianFormatter),
            hijri = "${hijri.day} ${hijriMonths[hijri.month - 1]} ${hijri.year} H · Islamic Civil",
            javanese = "${java.day} ${java.month.label} ${java.year} · Weton ${java.pasaran}"
        )
    }
}
