package com.example.appweather.data.remote

import com.example.appweather.data.remote.dto.ForecastResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
    @GET("forecast.json")
    suspend fun getWeatherForecast(
        @Query("q") location: String,
        @Query("days") days: Int = 3,
//        @Query("alerts") alerts: String = "yes",  //todo
        @Query("lang") lang: String? = "ru",
    ): ForecastResponse

}