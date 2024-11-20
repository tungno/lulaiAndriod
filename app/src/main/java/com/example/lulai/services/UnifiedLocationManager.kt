package com.example.lulai.services

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import com.google.android.gms.location.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class UnifiedLocationManager(context: Context) {
    private val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
    private val _userLatitude = MutableStateFlow(0.0)
    private val _userLongitude = MutableStateFlow(0.0)
    private val _isLocationFetched = MutableStateFlow(false)

    val userLatitude: StateFlow<Double> = _userLatitude
    val userLongitude: StateFlow<Double> = _userLongitude
    val isLocationFetched: StateFlow<Boolean> = _isLocationFetched

    init {
        fetchLocation()
    }

    @SuppressLint("MissingPermission")
    fun fetchLocation() {
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000).build()

        fusedLocationProviderClient.requestLocationUpdates(locationRequest, object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation?.let { location ->
                    _userLatitude.value = location.latitude
                    _userLongitude.value = location.longitude
                    _isLocationFetched.value = true
                }
                fusedLocationProviderClient.removeLocationUpdates(this)
            }
        }, null)
    }
}
