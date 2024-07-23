package com.example.weathertracker.permissionlauncher

import androidx.lifecycle.ViewModel
import com.example.weathertracker.permissionlauncher.data.Location
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

// viewModel to update the location data
class LocationViewModel: ViewModel() {
    private val _location = MutableStateFlow(LocationUi())
    val location = _location.asStateFlow()

    fun updateLocation(location: Location) {
        _location.update {
            it.copy(
                locationData = location
            )
        }
    }
}

// data class to store location data and give it to our api
data class LocationUi(
    val locationData: Location? = null
)