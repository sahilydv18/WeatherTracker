package com.example.weathertracker.remote.data

data class Day(
    val avghumidity: Int,
    val avgtemp_c: Double,
    val condition: Condition,
    val daily_chance_of_rain: Int,
    val maxwind_kph: Double,
    val totalprecip_mm: Double,
    val uv: Double
)