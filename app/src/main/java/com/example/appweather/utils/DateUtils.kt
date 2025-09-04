package com.example.appweather.utils

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

object DateUtils {

    private val dateTimeFormatter by lazy { DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm") }
    private val dateOnlyFormatter by lazy { DateTimeFormatter.ofPattern("yyyy-MM-dd") }
    private val timeFormatter by lazy { DateTimeFormatter.ofPattern("HH:mm") }
    private val dateFormatter by lazy { DateTimeFormatter.ofPattern("d MMMM", Locale.getDefault()) }
    private val weekDayFormatter by lazy {
        DateTimeFormatter.ofPattern(
            "EEEE",
            Locale.getDefault()
        )
    }

    fun parseApi(raw: String): LocalDateTime? = try {
        when (raw.length) {
            10 -> LocalDate.parse(raw, dateOnlyFormatter).atStartOfDay()
            16 -> LocalDateTime.parse(raw, dateTimeFormatter)
            else -> null
        }
    } catch (_: Exception) {
        null
    }

    fun formatDate(dateTime: LocalDateTime): String = dateTime.format(dateFormatter)

    fun formatWeekDay(dateTime: LocalDateTime): String =
        dateTime.format(weekDayFormatter).replaceFirstChar { it.uppercaseChar() }

    fun formatTime(dateTime: LocalDateTime): String = dateTime.format(timeFormatter)

    fun formatFromApi(raw: String, pattern: String, fallback: String = raw): String =
        parseApi(raw)?.format(DateTimeFormatter.ofPattern(pattern)) ?: fallback

    fun formatTimeHHmm(raw: String): String = formatFromApi(raw, "HH:mm", raw.takeLast(5))

    fun formatDateFromApi(raw: String): String = formatFromApi(raw, "d MMMM", raw)

    fun formatDateShortFromApi(raw: String): String = formatFromApi(raw, "d MMM", raw)

    fun formatWeekDayFromApi(raw: String): String =
        formatFromApi(raw, "EEEE", raw).replaceFirstChar { it.uppercaseChar() }
}
