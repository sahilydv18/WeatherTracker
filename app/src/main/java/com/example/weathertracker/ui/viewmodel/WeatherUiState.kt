package com.example.weathertracker.ui.viewmodel

import com.example.weathertracker.remote.data.WeatherData

sealed interface WeatherUiState {
    data class Success(val weatherData: WeatherData): WeatherUiState
    data object Loading: WeatherUiState
    data object Error: WeatherUiState
}
