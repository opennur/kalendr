package com.kalendr.app.calendar

import java.time.LocalDate
import java.time.YearMonth
import com.google.gson.JsonParser

enum class IndonesianDayType {
    NATIONAL_HOLIDAY,
    JOINT_LEAVE,
}

data class IndonesianHoliday(
    val date: LocalDate,
    val name: String,
    val type: IndonesianDayType,
)

object IndonesianHolidays {
    fun parse(body: String): List<IndonesianHoliday> {
        val root = JsonParser.parseString(body).asJsonObject
        check(root.get("success").asBoolean) { "Holiday API returned an unsuccessful response" }

        return root.getAsJsonArray("data").mapNotNull { element ->
            val item = element.asJsonObject
            val type = when (item.get("type").asString) {
                "holiday" -> IndonesianDayType.NATIONAL_HOLIDAY
                "leave" -> IndonesianDayType.JOINT_LEAVE
                else -> null
            }
            type?.let {
                IndonesianHoliday(
                    date = LocalDate.parse(item.get("date").asString),
                    name = item.get("name").asString,
                    type = it,
                )
            }
        }
    }

    fun parseNager(body: String, month: YearMonth): List<IndonesianHoliday> =
        JsonParser.parseString(body).asJsonArray.mapNotNull { element ->
            val item = element.asJsonObject
            val date = LocalDate.parse(item.get("date").asString)
            if (YearMonth.from(date) != month) return@mapNotNull null
            val isPublicHoliday = item.getAsJsonArray("types").any { it.asString == "Public" }
            if (!isPublicHoliday) return@mapNotNull null
            IndonesianHoliday(date, item.get("localName").asString, IndonesianDayType.NATIONAL_HOLIDAY)
        }
}
