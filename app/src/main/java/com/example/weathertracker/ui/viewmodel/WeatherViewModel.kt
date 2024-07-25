package com.example.weathertracker.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weathertracker.remote.repository.WeatherRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherRepo: WeatherRepo
): ViewModel() {

    private var _weatherUiState: WeatherUiState by mutableStateOf(WeatherUiState.Loading)
    val weatherUiState = _weatherUiState

    fun getWeatherData(apiKey: String, latAndLong: String): String {
        var string = ""
        viewModelScope.launch(Dispatchers.IO) {
//            _weatherUiState = try {
//                val weatherData = weatherRepo.getWeatherDate(apiKey,latAndLong)
//                WeatherUiState.Success(weatherData.toString())
//            } catch (e: IOException) {
//                WeatherUiState.Error
//            }
            string = weatherRepo.getWeatherDate(apiKey,latAndLong,"yes").toString()
            Log.d("ApiResponse",string)
        }
        return string
    }
}
