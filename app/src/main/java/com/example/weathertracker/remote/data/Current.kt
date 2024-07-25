package com.example.weathertracker.remote.data

data class Current(
    val condition: Condition,
    val feelslike_c: Double,
    val humidity: Int,
    val last_updated: String,
    val precip_mm: Double,
    val temp_c: Double,
    val uv: Double,
    val vis_km: Double,
    val wind_kph: Double
)