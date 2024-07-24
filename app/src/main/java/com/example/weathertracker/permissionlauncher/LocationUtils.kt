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
import kotlinx.coroutines.delay
import java.util.Locale
import javax.inject.Inject

class LocationUtils @Inject constructor(
    private val context: Context
) {
    // It will be used to get location data
    private val _fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

    val locationList: MutableList<Location> = mutableListOf()

    // function to request for location update using the FusedLocationProviderClient
    @SuppressLint("MissingPermission")
    suspend fun updateLocation(locationViewModel: LocationViewModel) {
        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(newLocation: LocationResult) {
                super.onLocationResult(newLocation)
                newLocation.lastLocation?.let {
                    val roundedLat = String.format(Locale.getDefault(), "%.3f", it.latitude).toDouble()
                    val roundedLong = String.format(Locale.getDefault(), "%.3f", it.longitude).toDouble()
                    val newLocationData = Location(latitude = roundedLat, longitude = roundedLong)
                    //Log.d("Updating Location", newLocationData.toString())
                    locationList.add(newLocationData)
                    //Log.d("AddToList", "Done")
                    if (locationList.size > 1) {
                        locationViewModel.updateLocation(locationList)
                    }
                    //locationViewModel.updateLocation(newLocationData)
                }
            }
        }

        // requesting for location
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000).build()

        _fusedLocationClient.requestLocationUpdates(locationRequest,locationCallback, Looper.getMainLooper())

        delay(6000)
        //Log.d("Delay","This should stop it for 6 s")

        _fusedLocationClient.removeLocationUpdates(locationCallback)
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