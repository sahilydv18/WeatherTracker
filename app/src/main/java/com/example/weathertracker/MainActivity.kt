package com.example.weathertracker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.weathertracker.permissionmanager.LocationPermissionLauncher
import com.example.weathertracker.permissionmanager.LocationUtils
import com.example.weathertracker.ui.theme.WeatherTrackerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val locationUtils = LocationUtils(this)
        setContent {
            WeatherTrackerTheme {
                val context = LocalContext.current
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier.padding(innerPadding).fillMaxSize()
                    ) {
                        if (locationUtils.isLocationPermissionGranted()) {

                        } else {
                            LocationPermissionLauncher(
                                context = context,
                                goToAppSetting = {
                                    goToAppSettings()
                                }
                            )
                        }
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