package br.com.transferr.services

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.util.Log
import br.com.transferr.extensions.log
import br.com.transferr.extensions.toJson
import br.com.transferr.main.util.Prefes
import br.com.transferr.model.responses.OnResponseInterface
import br.com.transferr.model.responses.RequestCoordinatesUpdate
import br.com.transferr.webservices.CoordinateService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class LocationTrackingService : Service(){

    private var mLocationManager: LocationManager? = null
    override fun onBind(intent: Intent?) = null
    private val scheduler = Executors.newScheduledThreadPool(1)
    private val SECONDS = 15L
    private val updateLocation=scheduler.scheduleAtFixedRate({buildLocationAPI()}, 10, SECONDS, TimeUnit.SECONDS)

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        return START_STICKY
    }

    override fun stopService(name: Intent?): Boolean {
        if(scheduler != null && updateLocation != null) {
            scheduler.schedule({ updateLocation.cancel(true) }, 5, TimeUnit.SECONDS)
        }
        return super.stopService(name)
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
        mLocationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        var location = mLocationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        if(location != null) {
            Log.d(TAG, "My Location on  buildLocationAPI method ${location?.latitude} - ${location?.longitude}")
            callWebService(location)
        }else{
            Log.d(TAG, "Cannot get the location o build API, it was null.")
        }
    }

}