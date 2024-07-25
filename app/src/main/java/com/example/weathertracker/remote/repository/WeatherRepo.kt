package com.example.weathertracker.remote.repository

import com.example.weathertracker.remote.data.WeatherData

interface WeatherRepo {
    suspend fun getWeatherDate(
        apiKey: String,
        latAndLong: String,
        aqi: String
    ): WeatherData
}