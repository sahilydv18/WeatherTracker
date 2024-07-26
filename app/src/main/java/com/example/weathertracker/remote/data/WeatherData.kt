package com.example.weathertracker.remote.data

data class WeatherData(
    val current: Current,
    val forecast: Forecast,
    val location: Location
)