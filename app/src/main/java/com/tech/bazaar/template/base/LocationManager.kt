package com.tech.bazaar.template.base

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import javax.inject.Inject

class LocationManager @Inject constructor(
    private val context: Context,
) {

    var myLocation: Location? = null

    private lateinit var locationRequest: LocationRequest

    fun getCurrentLocation(success: (result: Location?) -> Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
                == PackageManager.PERMISSION_GRANTED
            ) {
                //Location Permission already granted
                getFusedCurrentLocation(success)
            }
        } else {
            getFusedCurrentLocation(success)
        }
    }

    @SuppressLint("MissingPermission")
    private fun getFusedCurrentLocation(success: (result: Location) -> Unit) {

        val fusedLocationClient =
            LocationServices.getFusedLocationProviderClient(context)
        var locationCallback: LocationCallback? = null

        locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = UPDATE_INTERVAL
        locationRequest.fastestInterval = FASTEST_INTERVAL

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                if (location == null) {
                    locationCallback = object : LocationCallback() {
                        override fun onLocationResult(locationResult: LocationResult?) {
                            if (locationResult != null && locationResult.locations.isNotEmpty()) {
                                val currentLocation = locationResult.locations[0]
                                currentLocation?.let { it: Location ->
                                    fusedLocationClient.removeLocationUpdates(
                                        locationCallback
                                    )
                                    // Logic to handle location object
                                    myLocation = it
                                    success.invoke(it)
                                }
                            }
                        }
                    }
                    fusedLocationClient.requestLocationUpdates(
                        locationRequest,
                        locationCallback,
                        null
                    )
                } else {
                    // Logic to handle location object
                    myLocation = location
                    success.invoke(location)
                }
            }

    }

    companion object {
        private const val UPDATE_INTERVAL = 5000L
        private const val FASTEST_INTERVAL = 1000L
    }

}