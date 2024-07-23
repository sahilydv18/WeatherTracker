package com.example.weathertracker.permissionlauncher

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Looper
import android.util.Log
import androidx.core.content.ContextCompat
import com.example.weathertracker.permissionlauncher.data.Location
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import javax.inject.Inject

class LocationUtils @Inject constructor(
    private val context: Context
) {
    // It will be used to get location data
    private val _fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

    // function to request for location update using the FusedLocationProviderClient
    @SuppressLint("MissingPermission")
    fun updateLocation(locationViewModel: LocationViewModel) {
        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(newLocation: LocationResult) {
                super.onLocationResult(newLocation)
                newLocation.lastLocation?.let {
                    val newLocationData = Location(latitude = it.latitude, longitude = it.longitude)
                    Log.d("Updating Location", newLocationData.toString())
                    locationViewModel.updateLocation(newLocationData)
                }
            }
        }

        // requesting for location
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000).build()

        _fusedLocationClient.requestLocationUpdates(locationRequest,locationCallback, Looper.getMainLooper())
    }

    // function to check if the user has given location permission or not
    fun isLocationPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
                &&
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }
}