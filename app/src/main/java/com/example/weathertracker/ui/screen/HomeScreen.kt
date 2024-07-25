package com.example.weathertracker.ui.screen

import android.content.Context
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.weathertracker.BuildConfig
import com.example.weathertracker.permissionlauncher.LocationPermissionLauncher
import com.example.weathertracker.permissionlauncher.LocationUi
import com.example.weathertracker.permissionlauncher.LocationUtils
import com.example.weathertracker.permissionlauncher.LocationViewModel
import com.example.weathertracker.ui.viewmodel.WeatherUiState
import com.example.weathertracker.ui.viewmodel.WeatherViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Composable
fun HomeScreen(
    context: Context,
    goToAppSettings: () -> Unit,
    locationViewModel: LocationViewModel,
    locationUtils: LocationUtils,
    weatherViewModel: WeatherViewModel
) {
    val location: LocationUi by locationViewModel.location.collectAsState()

    val weatherUiState: WeatherUiState = weatherViewModel.weatherUiState

    // making sure that we get correct updated location of the user before giving it to api
    if (location.locationData.size > 1) {
        val latAndLong = "${location.locationData[1].latitude},${location.locationData[1].longitude}"
        weatherViewModel.getWeatherData(BuildConfig.API_KEY, latAndLong)
    }

    when(weatherUiState) {
        is WeatherUiState.Success -> {
            SuccessScreen(weatherData = weatherUiState.weatherData)
        }
        WeatherUiState.Error -> {
            Text(text = "Error")
        }
        WeatherUiState.Loading -> {
            CircularProgressIndicator()
        }
    }

    if (locationUtils.isLocationPermissionGranted()) {
        // Getting user location on background(IO) thread to make sure it doesn't interrupt main thread
        LaunchedEffect(key1 = Unit) {
            CoroutineScope(Dispatchers.IO).launch {
                locationUtils.updateLocation(locationViewModel)
            }
        }
    } else {
        LocationPermissionLauncher(
            context = context,
            goToAppSetting = {
                goToAppSettings()
            },
            getLocationUpdates = {
                // Getting user location on background(IO) thread to make sure it doesn't interrupt main thread
                CoroutineScope(Dispatchers.IO).launch {
                    locationUtils.updateLocation(locationViewModel)
                }
            }
        )
    }
}
