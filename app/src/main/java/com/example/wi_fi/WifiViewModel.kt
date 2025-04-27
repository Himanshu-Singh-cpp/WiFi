package com.example.wi_fi

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class WifiViewModel : ViewModel() {
    private val _locations = mutableStateListOf<LocationData>()
    val locations: List<LocationData> = _locations

    private val _currentReadings = mutableStateListOf<WifiReading>()
    val currentReadings: List<WifiReading> = _currentReadings

    private val _selectedLocationIndex = mutableStateOf(0)
    val selectedLocationIndex: State<Int> = _selectedLocationIndex

    init {
        // Initialize with three locations
        _locations.add(LocationData("Location 1"))
        _locations.add(LocationData("Location 2"))
        _locations.add(LocationData("Location 3"))
    }

    fun selectLocation(index: Int) {
        if (index in _locations.indices) {
            _selectedLocationIndex.value = index
        }
    }

    fun saveCurrentReadings() {
        val currentLocation = _locations[_selectedLocationIndex.value]
        _locations[_selectedLocationIndex.value] = currentLocation.copy(
            readings = _currentReadings.toList()
        )
    }

    fun updateReadings(readings: List<WifiReading>) {
        _currentReadings.clear()
        _currentReadings.addAll(readings)
    }
}