package com.kalendr.app

import android.graphics.Color
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import com.kalendr.app.calendar.IndonesianDayType
import com.kalendr.app.calendar.IndonesianHoliday

object IndonesianHolidayPresentation {
    fun marker(holiday: IndonesianHoliday): String = when (holiday.type) {
        IndonesianDayType.NATIONAL_HOLIDAY -> "LIBUR"
        IndonesianDayType.JOINT_LEAVE -> "CUTI"
    }

    fun color(type: IndonesianDayType): Int = when (type) {
        IndonesianDayType.NATIONAL_HOLIDAY -> Color.rgb(198, 40, 40)
        IndonesianDayType.JOINT_LEAVE -> Color.rgb(46, 125, 50)
    }

    fun summary(holidays: List<IndonesianHoliday>, loading: Boolean, error: Boolean): CharSequence {
        if (loading) return "Memuat hari besar dan hari libur..."
        if (error) return "Hari besar/libur tidak dapat dimuat dari API."
        if (holidays.isEmpty()) return "Tidak ada hari besar atau hari libur bulan ini."

        val summary = SpannableStringBuilder("Hari besar/libur bulan ini:\n")
        holidays.forEachIndexed { index, holiday ->
            val category = if (holiday.type == IndonesianDayType.NATIONAL_HOLIDAY) {
                "Libur nasional"
            } else {
                "Cuti bersama"
            }
            val line = "${holiday.date.dayOfMonth} ${holiday.name} · $category"
            val start = summary.length
            summary.append(line)
            summary.setSpan(
                ForegroundColorSpan(color(holiday.type)),
                start,
                summary.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE,
            )
            if (index < holidays.lastIndex) summary.append('\n')
        }
        return summary
    }
}
