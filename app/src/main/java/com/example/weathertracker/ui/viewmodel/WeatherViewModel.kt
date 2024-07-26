package com.example.weathertracker.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weathertracker.remote.repository.WeatherRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

private const val DAYS = 3;
private const val AQI = "no";
private const val ALERTS = "no";

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherRepo: WeatherRepo,
): ViewModel() {
    // ui state to show data on UI
    var weatherUiState: WeatherUiState by mutableStateOf(WeatherUiState.Loading)
        private set

    fun getWeatherData(apiKey: String, latAndLong: String) {
        // getting weather data on background(IO) thread
        viewModelScope.launch(Dispatchers.IO) {
            weatherUiState = try {
                val weatherData = weatherRepo.getWeatherData(apiKey,latAndLong, DAYS, AQI, ALERTS)
                WeatherUiState.Success(weatherData)
            } catch (e: IOException) {
                WeatherUiState.Error
            }
        }
    }
}
