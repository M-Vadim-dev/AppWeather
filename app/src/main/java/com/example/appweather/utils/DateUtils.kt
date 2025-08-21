package com.example.appweather.utils

import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

object DateUtils {

    private val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

    private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    /**
     * Парсим строку из API в LocalDateTime
     */
    fun parseApiDateTime(raw: String): LocalDateTime = LocalDateTime.parse(raw, inputFormatter)

    /**
     * Парсим время из API формата "2024-01-20 12:00" в LocalTime
     */
    fun parseHourTimeFromApi(timeString: String): LocalTime {
        return try {
            val dateTime = DateUtils.parseApiDateTime(timeString)
            dateTime.toLocalTime()
        } catch (e: Exception) {
            try {
                LocalTime.parse(timeString)
            } catch (e: Exception) {
                LocalTime.MIN
            }
        }
    }

    fun formatDate(dateTime: LocalDateTime): String {
        val formatter = DateTimeFormatter.ofPattern("d MMMM", Locale.getDefault())
        return dateTime.format(formatter) //
    }

    fun formatWeekDay(dateTime: LocalDateTime): String {
        val formatter = DateTimeFormatter.ofPattern("EEEE", Locale.getDefault())
        val weekDay = dateTime.format(formatter)
        return weekDay.replaceFirstChar { it.uppercaseChar() }
    }

    /**
     * Форматируем только время "HH:mm"
     */
    fun formatTime(dateTime: LocalDateTime): String = dateTime.format(timeFormatter)
}
