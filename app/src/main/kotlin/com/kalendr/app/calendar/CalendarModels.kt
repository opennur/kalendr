package com.kalendr.app.calendar

data class HijriDate(val year: Int, val month: Int, val day: Int)

enum class JavaneseMonth(val label: String) {
    SURA("Sura"),
    SAPAR("Sapar"),
    MULUD("Mulud"),
    BAKDA_MULUD("Bakda Mulud"),
    JUMADILAWAL("Jumadilawal"),
    JUMADILAKIR("Jumadilakir"),
    REJEB("Rejeb"),
    RUWAH("Ruwah"),
    PASA("Pasa"),
    SAWAL("Sawal"),
    SELA("Sela"),
    BESAR("Besar")
}

data class JavaneseDate(
    val year: Int,
    val month: JavaneseMonth,
    val day: Int,
    val pasaran: String
)

data class CalendarDisplay(val gregorian: String, val hijri: String, val javanese: String)
