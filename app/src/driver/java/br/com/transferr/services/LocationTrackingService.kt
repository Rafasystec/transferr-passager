package br.com.transferr.services

import android.Manifest
import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.widget.Toast
import br.com.transferr.extensions.log
import br.com.transferr.extensions.toJson
import br.com.transferr.model.responses.OnResponseInterface
import br.com.transferr.model.responses.RequestCoordinatesUpdate
import br.com.transferr.util.NetworkUtil
import br.com.transferr.util.Prefes
import br.com.transferr.webservices.CoordinateService
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import org.jetbrains.anko.toast

class LocationTrackingService : Service(),com.google.android.gms.location.LocationListener,GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener {
    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        Log.d(TAG, "Connection failed. Error: " + connectionResult.errorCode)
    }

    @SuppressLint("MissingPermission")
    override fun onConnected(p0: Bundle?) {
        Log.d(TAG,"onConnected call")
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        startLocationUpdates()
    }

    override fun onConnectionSuspended(p0: Int) {
        mGoogleApiClient.connect()
    }

    override fun onLocationChanged(location: Location?) {
        callWebService(location)
    }


    private lateinit var mGoogleApiClient: GoogleApiClient
    private var mLocationRequest: LocationRequest?  = null
    private val UPDATE_INTERVAL = (2 * 1000).toLong()  /* 10 secs */
    private val FASTEST_INTERVAL: Long = 2000 /* 2 sec */
    private var mLocationManager: LocationManager? = null

    override fun onBind(intent: Intent?) = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        return START_STICKY
    }

    private val TAG = "LocationTrackingService"
    @SuppressLint("MissingPermission")
    override fun onCreate() {
        Log.d(TAG,"on create")
        buildLocationAPI()
    }

    private fun callWebService(location: Location?) {


        var requestUpdate = RequestCoordinatesUpdate()
        requestUpdate.longitude = location?.longitude
        requestUpdate.latitude  = location?.latitude
        requestUpdate.idCar     = Prefes.prefsCar
        CoordinateService.updateMyLocation(requestUpdate,
                object : OnResponseInterface<RequestCoordinatesUpdate>{
                    override fun onSuccess(body: RequestCoordinatesUpdate?) {
                        log("OK! JSON returned --> ${body?.toJson()}")
                    }

                    override fun onError(message: String) {
                        log("Error : $message")
                    }

                    override fun onFailure(t: Throwable?) {
                        if(t != null) {
                            log("Failure to update my location: ${t.message}")
                        }else{
                            log("Failure to update my location")
                        }
                    }
                })
    }



    @SuppressLint("MissingPermission")
    private fun buildLocationAPI(){
        mGoogleApiClient = GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build()
        mGoogleApiClient.connect()
        mLocationRequest = LocationRequest()
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)

        mLocationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        var location = mLocationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        if(location != null) {
            Log.d(TAG, "My Location on  buildLocationAPI method ${location?.latitude} - ${location?.longitude}")
            callWebService(location)
        }else{
            Log.d(TAG, "Cannot get the location o build API, it was null.")
        }

    }


    private fun startLocationUpdates() {

        // Create the location request
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);
        // Request location updates
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest, this)

    }


    override fun onDestroy() {
        super.onDestroy()
        //toast("On destroy")
        if(mGoogleApiClient != null){
            if(mGoogleApiClient.isConnected){
                mGoogleApiClient.disconnect()
            }
        }
    }

}