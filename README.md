# WiFi Signal Strength Logger

A Kotlin/Android application that scans, logs, and visualizes WiFi signal strength data across different locations.

## Features

### 1. User Interface (10 marks)
- Clean Material Design 3 implementation using Jetpack Compose
- Intuitive layout with location tabs, action buttons, and signal visualization
- Color-coded signal strength indicators providing visual feedback
- Responsive UI that adapts to different device sizes


### 2. Data Logging Capabilities (15 marks)
- Real-time WiFi scanning with WifiManager
- Captures key network information:
  - SSID (network name)
  - BSSID (MAC address)
  - RSSI (signal strength in dBm)
- Permission handling for location and WiFi state access
- Save button to store readings for each location
- Automatic handling of hidden networks


### 3. Multi-Location Comparison (15 marks)
- Support for three distinct location profiles
- Easy switching between locations via tab interface
- Location comparison chart showing:
  - Average signal strength per location
  - Signal strength range (min to max)
  - Visual bar representation of relative signal strength
- Color-coded visualization making it easy to identify signal quality differences


## How to Use

1. Launch the app and grant necessary permissions
2. Select a location tab (Location 1, 2, or 3)
3. Press "Scan WiFi" to detect nearby networks
4. Review the current readings displayed in the list
5. Press "Save Readings" to store data for the selected location
6. Repeat for other locations to build a comparison dataset
7. View the comparison chart at the bottom to analyze differences

## Technical Implementation

- Built with Kotlin and Jetpack Compose
- MVVM architecture with ViewModel for state management
- BroadcastReceiver for WiFi scan results
- Real-time UI updates with Compose's reactive state system
- Efficient data structures for storing and comparing readings

## Requirements

- Android device running Android 8.0 (API level 26) or higher
- Location and WiFi permissions

## Installation

1. Clone the repository
2. Open in Android Studio
3. Build and run on your device or emulator

## Future Enhancements

- Export/import of location data
- Historical data tracking
- Machine learning-based location prediction
