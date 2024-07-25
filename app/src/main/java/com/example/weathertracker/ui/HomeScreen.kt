package com.example.weathertracker.ui

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weathertracker.permissionlauncher.LocationPermissionLauncher
import com.example.weathertracker.permissionlauncher.LocationUi
import com.example.weathertracker.permissionlauncher.LocationUtils
import com.example.weathertracker.permissionlauncher.LocationViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Composable
fun HomeScreen(
    context: Context,
    goToAppSettings: () -> Unit,
    locationViewModel: LocationViewModel = viewModel(),
    locationUtils: LocationUtils
) {
    val location: LocationUi by locationViewModel.location.collectAsState()

    //Log.d("HomeScreenListSize", location.locationData.size.toString())
    if(location.locationData.size > 1) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "${location.locationData[1].latitude}")
            Text(text = "${location.locationData[1].longitude}")
        }
    } else {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
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
