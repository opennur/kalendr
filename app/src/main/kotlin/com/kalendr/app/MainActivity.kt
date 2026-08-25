package com.kalendr.app

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.kalendr.app.calendar.HijriConverter
import com.kalendr.app.calendar.JavaneseConverter
import com.kalendr.app.calendar.MonthCalendar
import com.kalendr.app.calendar.DateSelection
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private var today = LocalDate.now()
    private var visibleMonth = YearMonth.from(today)
    private lateinit var monthTitle: TextView
    private lateinit var calendarGrid: LinearLayout
    private val locale = Locale("id", "ID")
    private val accessibilityDateFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy", locale)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(createContent())
        renderMonth()
    }

    override fun onResume() {
        super.onResume()
        today = LocalDate.now()
        if (::monthTitle.isInitialized) renderMonth()
    }

    private fun createContent(): ScrollView {
        val content = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(16), dp(12), dp(16), dp(16))
            setBackgroundColor(Color.rgb(247, 244, 238))
        }
        content.addView(text("KALENDR", 24f, Color.rgb(16, 42, 67)).apply {
            gravity = Gravity.CENTER
        })
        content.addView(text("Hijriah dan Jawa dalam satu bulan", 13f, Color.DKGRAY).apply {
            gravity = Gravity.CENTER
        }, margins(0, 0, 0, 8))
        monthTitle = text("", 20f, Color.rgb(16, 42, 67)).apply {
            gravity = Gravity.CENTER
            isSingleLine = true
        }
        content.addView(monthTitle, margins(0, 0, 0, 4))
        val actions = LinearLayout(this).apply {
            gravity = Gravity.CENTER
            orientation = LinearLayout.HORIZONTAL
        }
        actions.addView(button("Hari ini", "Kembali ke hari ini") {
            visibleMonth = YearMonth.from(today)
            renderMonth()
        }, LinearLayout.LayoutParams(0, dp(42), 1f).apply { marginEnd = dp(4) })
        actions.addView(button("Pilih tanggal", "Buka pemilih tanggal") {
            showDatePicker()
        }, LinearLayout.LayoutParams(0, dp(42), 1f).apply { marginStart = dp(4) })
        content.addView(actions, margins(0, 0, 0, 12))
        content.addView(dayHeader(), margins(0, 0, 0, 2))
        calendarGrid = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL }
        content.addView(MaterialCardView(this).apply {
            radius = dp(16).toFloat()
            cardElevation = 0f
            setCardBackgroundColor(Color.WHITE)
            setContentPadding(dp(8), dp(8), dp(8), dp(8))
            addView(calendarGrid)
        })
        content.addView(text("Angka kecil: tanggal Hijriah · tanggal Jawa / pasaran", 12f, Color.DKGRAY), margins(0, 12, 0, 0))
        return ScrollView(this).apply { addView(content) }
    }

    private fun renderMonth() {
        val isCurrentMonth = visibleMonth == YearMonth.from(today)
        val monthLabel = visibleMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy", locale))
        monthTitle.text = if (isCurrentMonth) "$monthLabel · Sekarang" else monthLabel
        monthTitle.setTextColor(if (isCurrentMonth) Color.rgb(23, 107, 135) else Color.rgb(16, 42, 67))
        calendarGrid.removeAllViews()
        MonthCalendar.build(visibleMonth).cells.chunked(7).forEach { week ->
            val row = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL }
            week.forEach { cell -> row.addView(dayCell(cell.date), cellParams()) }
            calendarGrid.addView(row, margins(0, 0, 0, 3))
        }
    }

    private fun showDatePicker() {
        val selectedMillis = DateSelection.toUtcMillis(visibleMonth.atDay(1))
        val picker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Pilih tanggal")
            .setSelection(selectedMillis)
            .setCalendarConstraints(
                CalendarConstraints.Builder()
                    .setOpenAt(selectedMillis)
                    .build()
            )
            .build()
        picker.addOnPositiveButtonClickListener { millis ->
            visibleMonth = YearMonth.from(DateSelection.fromUtcMillis(millis))
            renderMonth()
        }
        picker.show(supportFragmentManager, "date-picker")
    }

    private fun dayHeader(): LinearLayout = LinearLayout(this).apply {
        listOf("MIN", "SEN", "SEL", "RAB", "KAM", "JUM", "SAB").forEach { label ->
            addView(text(label, 11f, Color.DKGRAY).apply { gravity = Gravity.CENTER }, cellParams())
        }
    }

    private fun dayCell(date: LocalDate?): TextView = text("", 11f, Color.rgb(16, 42, 67)).apply {
        gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
        setPadding(dp(2), dp(6), dp(2), dp(4))
        minHeight = dp(64)
        if (date == null) {
            visibility = View.INVISIBLE
        } else {
            val hijri = HijriConverter.convert(date)
            val javanese = JavaneseConverter.convert(date)
            val isToday = date == today
            text = "${date.dayOfMonth}\n${hijri.day}/${hijri.month}\n${javanese.day} ${javanese.pasaran.take(3)}"
            if (isToday) {
                setBackgroundColor(Color.rgb(23, 107, 135))
                setTextColor(Color.WHITE)
            }
            val fullDate = date.format(accessibilityDateFormatter)
            val todayLabel = if (isToday) "Hari ini, " else ""
            contentDescription = "$todayLabel$fullDate, ${hijri.day} Hijriah, ${javanese.day} Jawa ${javanese.pasaran}"
        }
    }

    private fun button(label: String, description: String, action: () -> Unit) = MaterialButton(this).apply {
        text = label
        textSize = 14f
        insetTop = 0
        insetBottom = 0
        minHeight = 0
        contentDescription = description
        setOnClickListener { action() }
    }

    private fun text(value: String, size: Float, color: Int) = TextView(this).apply {
        text = value
        textSize = size
        setTextColor(color)
    }

    private fun cellParams() = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)

    private fun dp(value: Int): Int = (value * resources.displayMetrics.density).toInt()

    private fun margins(left: Int, top: Int, right: Int, bottom: Int) =
        LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT).apply {
            setMargins(dp(left), dp(top), dp(right), dp(bottom))
        }
}
