package com.example.weathertracker.remote

import com.example.weathertracker.remote.data.WeatherData
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("/v1/forecast.json")
    suspend fun getWeatherData(
        @Query("key") apiKey: String,
        @Query("q") latAndLong: String,
        @Query("days") days: Int,
        @Query("aqi") aqi: String,
        @Query("alerts") alerts: String
    ): WeatherData
}