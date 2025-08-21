package com.example.appweather.utils

import com.example.appweather.R

object WindUtils {

    /**
     * Преобразует направление ветра в читаемый текст
     * @param direction Сокращение направления (e.g.: "NSW", "N", "SSE")
     * @return Читаемое направление ветра на русском
     */
    fun getWindDirectionText(direction: String): String {
        return when (direction.uppercase()) {
            "N" -> "Северный"
            "NNE" -> "Северо-северо-восточный"
            "NE" -> "Северо-восточный"
            "ENE" -> "Восточно-северо-восточный"
            "E" -> "Восточный"
            "ESE" -> "Восточно-юго-восточный"
            "SE" -> "Юго-восточный"
            "SSE" -> "Юго-юго-восточный"
            "S" -> "Южный"
            "SSW" -> "Юго-юго-западный"
            "SW" -> "Юго-западный"
            "WSW" -> "Западно-юго-западный"
            "W" -> "Западный"
            "WNW" -> "Западно-северо-западный"
            "NW" -> "Северо-западный"
            "NNW" -> "Северо-северо-западный"
            "NSW" -> "Северо-юго-западный"
            else -> direction
        }
    }

    /**
     * Получает иконку для направления ветра
     */
//    fun getWindDirectionIcon(direction: String): Int { //todo
//        return when (direction.uppercase()) {
//            "N" -> R.drawable.ic_wind_n
//            "NNE", "NE", "ENE" -> R.drawable.ic_wind_ne
//            "E" -> R.drawable.ic_wind_e
//            "ESE", "SE", "SSE" -> R.drawable.ic_wind_se
//            "S" -> R.drawable.ic_wind_s
//            "SSW", "SW", "WSW" -> R.drawable.ic_wind_sw
//            "W" -> R.drawable.ic_wind_w
//            "WNW", "NW", "NNW" -> R.drawable.ic_wind_nw
//            "NSW" -> R.drawable.ic_wind_sw // Для NSW используем юго-западную иконку
//            else -> R.drawable.ic_wind
//        }
//    }

    /**
     * Полная информация о ветре: скорость и направление
     */
    fun getWindInfo(windKph: Double, windDir: String): String {
        val direction = getWindDirectionText(windDir)
        return "$direction, ${windKph.toInt()} км/ч"
    }
}