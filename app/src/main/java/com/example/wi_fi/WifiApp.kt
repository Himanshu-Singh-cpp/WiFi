package com.example.wi_fi

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun WifiApp(viewModel: WifiViewModel, onScanClick: () -> Unit) {
    val selectedIndex by viewModel.selectedLocationIndex
    val currentReadings = viewModel.currentReadings

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text(
                text = "WiFi Signal Strength Logger",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Location selector tabs
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                viewModel.locations.forEachIndexed { index, location ->
                    OutlinedButton(
                        onClick = { viewModel.selectLocation(index) },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = if (selectedIndex == index)
                                MaterialTheme.colorScheme.primaryContainer
                            else
                                MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Text(location.name)
                    }
                }
            }

            // Action buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = onScanClick) {
                    Text("Scan WiFi")
                }

                Button(onClick = { viewModel.saveCurrentReadings() }) {
                    Text("Save Readings")
                }
            }

            // Display current readings
            Text(
                text = "Current Readings: ${currentReadings.size}",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Display the readings
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                items(currentReadings) { reading ->
                    WifiReadingItem(reading)
                }
            }

            // Compare locations
            if (viewModel.locations.any { it.readings.isNotEmpty() }) {
                Text(
                    text = "Location Comparison",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                LocationComparisonChart(viewModel.locations)
            }
        }
    }
}

@Composable
fun WifiReadingItem(reading: WifiReading) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = reading.ssid,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = reading.bssid,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Signal strength indicator
            Box(
                modifier = Modifier
                    .width(100.dp)
                    .height(24.dp)
                    .background(
                        color = SignalStrengthColor(reading.rssi),
                        shape = RoundedCornerShape(4.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${reading.rssi} dBm",
                    color = Color.White,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
fun SignalStrengthColor(rssi: Int): Color {
    return when {
        rssi > -50 -> Color.Green
        rssi > -70 -> Color(0xFF8BC34A)  // Light Green
        rssi > -80 -> Color(0xFFFFC107)  // Amber
        rssi > -90 -> Color(0xFFFF9800)  // Orange
        else -> Color(0xFFF44336)        // Red
    }
}

@Composable
fun LocationComparisonChart(locations: List<LocationData>) {
    val datasets = locations.map { location ->
        if (location.readings.isEmpty()) {
            null
        } else {
            val avgRssi = location.readings.map { it.rssi }.average().toFloat()
            val minRssi = location.readings.minOfOrNull { it.rssi } ?: 0
            val maxRssi = location.readings.maxOfOrNull { it.rssi } ?: 0

            Triple(location.name, avgRssi, minRssi to maxRssi)
        }
    }.filterNotNull()

    Column(modifier = Modifier.fillMaxWidth()) {
        datasets.forEach { (name, avg, range) ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = name,
                    modifier = Modifier.width(80.dp)
                )

                Column {
                    Text(
                        text = "Avg: ${avg.toInt()} dBm, Range: ${range.first} to ${range.second} dBm",
                        style = MaterialTheme.typography.bodySmall
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(20.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color.LightGray)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth(
                                    ((avg.toInt() + 100) / 100f).coerceIn(0f, 1f)
                                )
                                .background(SignalStrengthColor(avg.toInt()))
                        )
                    }
                }
            }
        }
    }
}