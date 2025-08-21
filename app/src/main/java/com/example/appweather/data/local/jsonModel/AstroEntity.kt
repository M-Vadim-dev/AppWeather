package com.example.appweather.data.local.jsonModel

import kotlinx.serialization.Serializable

@Serializable
data class AstroEntity(
    val sunrise: String,
    val sunset: String,
    val moonrise: String,
    val moonset: String,
    val moonPhase: String,
    val moonIllumination: String,
)
