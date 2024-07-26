package com.example.weathertracker.remote.repository

import com.example.weathertracker.remote.data.WeatherData

interface WeatherRepo {
    suspend fun getWeatherData(
        apiKey: String,
        latAndLong: String,
        days: Int,
        aqi: String,
        alerts: String
    ): WeatherData
}