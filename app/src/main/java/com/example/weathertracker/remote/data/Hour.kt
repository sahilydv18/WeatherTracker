package com.example.weathertracker.remote.data

data class Hour(
    val chance_of_rain: Int,
    val condition: ForecastCondition,
    val humidity: Int,
    val temp_c: Double,
    val time: String
)