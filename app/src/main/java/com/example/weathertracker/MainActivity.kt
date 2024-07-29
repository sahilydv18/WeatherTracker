package com.example.weathertracker

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weathertracker.permissionlauncher.LocationUtils
import com.example.weathertracker.permissionlauncher.LocationViewModel
import com.example.weathertracker.ui.screen.HomeScreen
import com.example.weathertracker.ui.theme.WeatherTrackerTheme
import com.example.weathertracker.ui.viewmodel.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@RequiresApi(Build.VERSION_CODES.O)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WeatherTrackerTheme {
                val context = LocalContext.current
                val locationUtils = LocationUtils(context)
                val locationViewModel: LocationViewModel = viewModel()
                val location by locationViewModel.location.collectAsState()
                val weatherViewModel: WeatherViewModel = hiltViewModel()
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                    ) {
                        HomeScreen(
                            context = context,
                            goToAppSettings = { goToAppSettings() },
                            locationUtils = locationUtils,
                            locationViewModel = locationViewModel,
                            weatherViewModel = weatherViewModel,
                            onRetryButtonClicked = {
                                locationUtils.updateLocation(locationViewModel)
                                if (location.locationData.size > 1) {
                                    val latAndLong = "${location.locationData[1].latitude},${location.locationData[1].longitude}"
                                    weatherViewModel.getWeatherData(BuildConfig.API_KEY, latAndLong)
                                }
                            }
                        )
                    }
                }
            }
        }
    }

    // function to open app settings used when the user declines permission for the second time
    private fun goToAppSettings() {
        Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", packageName, null)
        ).also(::startActivity)
    }
}