package com.example.weathertracker.ui

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weathertracker.permissionlauncher.LocationPermissionLauncher
import com.example.weathertracker.permissionlauncher.LocationUi
import com.example.weathertracker.permissionlauncher.LocationUtils
import com.example.weathertracker.permissionlauncher.LocationViewModel

@Composable
fun HomeScreen(
    context: Context,
    goToAppSettings: () -> Unit,
    locationViewModel: LocationViewModel = viewModel(),
) {
    val locationUtils = LocationUtils(context)

    val location: LocationUi by locationViewModel.location.collectAsState()

    if(location.locationData == null) {
        Column {
            Text(text = "No location available")
        }
    } else {
        Column {
            Text(text = "${location.locationData?.latitude}")
            Text(text = "${location.locationData?.longitude}")
        }
    }

    if (locationUtils.isLocationPermissionGranted()) {
        locationUtils.updateLocation(locationViewModel)
    } else {
        LocationPermissionLauncher(
            context = context,
            goToAppSetting = {
                goToAppSettings()
            },
            getLocationUpdates = {
                locationUtils.updateLocation(locationViewModel)
            }
        )
    }
}
