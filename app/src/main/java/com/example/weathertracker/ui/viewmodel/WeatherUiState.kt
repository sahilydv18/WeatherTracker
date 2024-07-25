package com.example.weathertracker.ui.viewmodel

sealed interface WeatherUiState {
    data class Success(val weatherData: String): WeatherUiState
    data object Loading: WeatherUiState
    data object Error: WeatherUiState
}
