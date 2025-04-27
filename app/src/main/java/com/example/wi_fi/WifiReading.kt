package com.example.wi_fi

data class WifiReading(
    val ssid: String,
    val bssid: String,
    val rssi: Int
)

data class LocationData(
    val name: String,
    val readings: List<WifiReading> = emptyList()
)