package br.com.transferr.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import com.google.android.gms.maps.model.LatLng


/**
 * Created by idoctor on 16/02/2018.
 */
class MyLocationLister:LocationListener {

    private var locationManager: LocationManager? = null
    private var bestLocation: Location? = null
    override fun onLocationChanged(location: Location?) {
        Log.d("LocationListener", "onLocationChanged")
        if(location != null){
            Log.d("LocationListener", "onLocationChanged LatLng ${location.latitude} - ${location.longitude}")
        }
    }

    override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {
        Log.d("LocationListener", "onStatusChanged")
    }

    override fun onProviderEnabled(p0: String?) {
        Log.d("LocationListener", "onProviderEnabled")
    }

    override fun onProviderDisabled(p0: String?) {
        Log.d("LocationListener", "onProviderDisabled")
    }

    @SuppressLint("MissingPermission")
    fun getLocation(context:Context): LatLng? {
        //if (!gpsIsEnabled()) {
        //    showGPSSettings()
        //    return null
        //} else {
            locationManager = context.getSystemService(LOCATION_SERVICE) as LocationManager
            var providers: List<String>? = null
            if (locationManager != null) {
                providers = locationManager?.getProviders(true)
            }
            //bestLocation = null
            if (providers != null) {
                for (provider in providers) {
                    val location = locationManager?.getLastKnownLocation(provider) ?: continue
                    if (bestLocation == null || location.getAccuracy() < bestLocation?.getAccuracy()!!) {
                        bestLocation = location
                    }
                }
            }
            var bestLatLngLocation: LatLng? = null
            if (bestLocation != null) {
                bestLatLngLocation = LatLng(bestLocation!!.getLatitude(), bestLocation!!.getLongitude())
            }
            return bestLatLngLocation
        //}
    }

}