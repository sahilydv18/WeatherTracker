package com.example.weathertracker.permissionlauncher

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Looper
import androidx.core.content.ContextCompat
import com.example.weathertracker.permissionlauncher.data.Location
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import java.util.Locale

class LocationUtils (
    private val context: Context
) {
    // It will be used to get location data
    private val _fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

    private lateinit var locationCallback: LocationCallback

    val locationList: MutableList<Location> = mutableListOf()

    // function to request for location update using the FusedLocationProviderClient
    @SuppressLint("MissingPermission")
    fun updateLocation(locationViewModel: LocationViewModel) {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(newLocation: LocationResult) {
                super.onLocationResult(newLocation)
                newLocation.lastLocation?.let {
                    val roundedLat = String.format(Locale.getDefault(), "%.4f", it.latitude).toDouble()
                    val roundedLong = String.format(Locale.getDefault(), "%.4f", it.longitude).toDouble()
                    val newLocationData = Location(latitude = roundedLat, longitude = roundedLong)
                    //Log.d("Updating Location", newLocationData.toString())
                    locationList.add(newLocationData)
                    //Log.d("AddToList", "Done")
                    if (locationList.size > 1) {
                        locationViewModel.updateLocation(locationList)
                    }
                    stopLocationRequest(locationList.size)
                }
            }
        }

        // this is a location request basically used for setting priority of location accuracy as we have set - PRIORITY_HIGH_ACCURACY, it will give us the most
        // accurate location and also we set here in how much time do we need to make call to location callback to get new location data
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1).build()

        // it request for location
        _fusedLocationClient.requestLocationUpdates(locationRequest,locationCallback, Looper.getMainLooper())
    }

    // function to stop requesting location when we got the updated user location, to stop unnecessary gms network calls
    private fun stopLocationRequest(size: Int) {
        if (size > 1) {
            //Log.d("Stop", "Stopping location callback $size")
            _fusedLocationClient.removeLocationUpdates(locationCallback)
        }
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