package com.kalendr.app.calendar

import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.time.YearMonth

class IndonesianHolidayApi {
    fun fetch(month: YearMonth): List<IndonesianHoliday> {
        val tanggalMerah = runCatching { fetchTanggalMerah(month) }.getOrNull()
        if (!tanggalMerah.isNullOrEmpty()) return tanggalMerah
        return fetchNager(month)
    }

    private fun fetchTanggalMerah(month: YearMonth): List<IndonesianHoliday> {
        val endpoint = URL(
            "https://tanggalmerah.upset.dev/api/holidays?year=${month.year}&month=${month.monthValue}",
        )
        val connection = endpoint.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        connection.connectTimeout = 10_000
        connection.readTimeout = 10_000
        return try {
            if (connection.responseCode != HttpURLConnection.HTTP_OK) {
                throw IOException("Holiday API returned HTTP ${connection.responseCode}")
            }
            connection.inputStream.bufferedReader().use { reader ->
                IndonesianHolidays.parse(reader.readText())
            }
        } finally {
            connection.disconnect()
        }
    }

    private fun fetchNager(month: YearMonth): List<IndonesianHoliday> {
        val endpoint = URL("https://date.nager.at/api/v3/PublicHolidays/${month.year}/ID")
        val connection = endpoint.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        connection.connectTimeout = 10_000
        connection.readTimeout = 10_000
        return try {
            if (connection.responseCode != HttpURLConnection.HTTP_OK) {
                throw IOException("Nager API returned HTTP ${connection.responseCode}")
            }
            connection.inputStream.bufferedReader().use { reader ->
                IndonesianHolidays.parseNager(reader.readText(), month)
            }
        } finally {
            connection.disconnect()
        }
    }
}
