package com.example.wi_fi

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.WifiManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.example.wi_fi.ui.theme.WiFiTheme

// Implementation as provided in previous response

class MainActivity : ComponentActivity() {
    private lateinit var wifiManager: WifiManager
    private val viewModel: WifiViewModel by viewModels()
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions.all { it.value }) {
            startWifiScanning()
        }
    }

    private val wifiScanReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == WifiManager.SCAN_RESULTS_AVAILABLE_ACTION) {
                val success = intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false)
                if (success) {
                    scanSuccess()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        wifiManager = getSystemService(Context.WIFI_SERVICE) as WifiManager

        requestPermissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.CHANGE_WIFI_STATE
            )
        )

        enableEdgeToEdge()
        setContent {
            WiFiTheme {
                WifiApp(viewModel) { startScan() }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val intentFilter = IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
        registerReceiver(wifiScanReceiver, intentFilter)
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(wifiScanReceiver)
    }

    private fun startWifiScanning() {
        // Initial scan
        startScan()
    }

    private fun startScan() {
        wifiManager.startScan()
    }

    private fun scanSuccess() {
        val results = wifiManager.scanResults
        val readings = results.take(100).map {
            WifiReading(
                ssid = it.SSID.ifEmpty { "<Hidden Network>" },
                bssid = it.BSSID,
                rssi = it.level
            )
        }
        viewModel.updateReadings(readings)
    }
}