package com.example.weathertracker.permissionlauncher

import android.Manifest
import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.core.app.ActivityCompat
import com.example.weathertracker.MainActivity
import com.example.weathertracker.R

// Permission Launcher
@Composable
fun LocationPermissionLauncher(
    context: Context,
    goToAppSetting: () -> Unit,
    getLocationUpdates: () -> Unit
) {
    // variable for showing rationale dialog, used when the user declines permission for the first time
    var showRationaleDialog by rememberSaveable {
        mutableStateOf(false)
    }

    // variable for showing settings dialog, used when the user declines permission for the second time
    var showSettingDialog by rememberSaveable {
        mutableStateOf(false)
    }

    // permission launcher for getting location permission
    val locationPermissionLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestMultiplePermissions()) { permission ->
            if (permission[Manifest.permission.ACCESS_FINE_LOCATION] == true && permission[Manifest.permission.ACCESS_COARSE_LOCATION] == true) {
                // Get location updates
                getLocationUpdates()
            } else {
                // Handling the case when the user declines to give permission
                val rationaleRequired = ActivityCompat.shouldShowRequestPermissionRationale(
                    context as MainActivity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) || ActivityCompat.shouldShowRequestPermissionRationale(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )

                if (rationaleRequired) {
                    // when the user declines the permission for first time, change this variable to true, so that we can show the rationale dialog
                    showRationaleDialog = true
                } else {
                    // when the user declines the permission for second time, change this variable to true, so that we can show the settings dialog
                    showSettingDialog = true
                }
            }
        }

    if (showRationaleDialog) {
        // Rationale Dialog, to show the user reason for asking permission when the user declines permission for the first time
        PermissionDeclineDialog(
            text = R.string.permission_reason,
            confirmButtonText = R.string.ok,
            onConfirmButtonClick = {
                // after user clicks the confirm button, then again launching the location permission launcher to ask again for permission
                locationPermissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            }
        )
    }

    if (showSettingDialog) {
        // Settings Dialog, to show the user a way to open settings and give location permission, as the user declined permission for the second time
        PermissionDeclineDialog(
            text = R.string.setting_reason,
            confirmButtonText = R.string.go_to_setting,
            // when user clicks the confirm button he will be redirected to app settings
            onConfirmButtonClick = goToAppSetting
        )
    }

    // using launched effect, because we are asking for permission on start of the app, so we delay our permission launcher till our main activity is formed
    // if this is not done then the app will crash, as our main activity is not yet formed and permission launcher is launched
    LaunchedEffect(Unit) {
        // launching our permission launcher for the first time on the start of our app
        locationPermissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }
}