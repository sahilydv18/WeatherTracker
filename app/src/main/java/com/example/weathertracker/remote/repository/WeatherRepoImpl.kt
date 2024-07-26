package com.example.weathertracker.remote.repository

import com.example.weathertracker.remote.WeatherApi
import com.example.weathertracker.remote.data.WeatherData
import javax.inject.Inject

class WeatherRepoImpl @Inject constructor(
    private val weatherApi: WeatherApi
): WeatherRepo {
    override suspend fun getWeatherData(
        apiKey: String,
        latAndLong: String,
        days: Int,
        aqi: String,
        alerts: String
    ): WeatherData {
        return weatherApi.getWeatherData(
            apiKey,
            latAndLong,
            days,
            aqi,
            alerts
        )
    }
}