package br.com.transferr.activities

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AlertDialog
import android.util.Log
import android.widget.Toast
//import br.com.transferr.fragments.MapsFragment
import br.com.transferr.model.Car
import br.com.transferr.model.Coordinates
import br.com.transferr.model.Driver
import br.com.transferr.model.enums.EnumStatus
import br.com.transferr.R
import br.com.transferr.passenger.activities.SuperClassActivity
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnSuccessListener
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread


class InitialActivity : br.com.transferr.passenger.activities.SuperClassActivity(), GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener  {

    private val TAG = "INITIAL_ACTIVITY"
    private lateinit var mGoogleApiClient: GoogleApiClient
    private var mLocationManager: LocationManager? = null
    lateinit var mLocation: Location
    private var mLocationRequest: LocationRequest? = null
    private val listener: com.google.android.gms.location.LocationListener? = null
    private val UPDATE_INTERVAL = (2 * 1000).toLong()  /* 10 secs */
    private val FASTEST_INTERVAL: Long = 2000 /* 2 sec */

    lateinit var locationManager: LocationManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_initial)

       // MultiDex.install(this)
        //TODO Quando for ver  parte do passageriro olhar para essa calsse de exemplo
        mGoogleApiClient = GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build()

        mLocationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        checkLocation()
        //val mapsFragment = MapsFragment()
        //mapsFragment.arguments = intent.extras
        //supportFragmentManager
         //       .beginTransaction()
          //      .replace(R.id.map,mapsFragment)
          //      .commit()
        //startService(Intent(this,SuperIntentService::class.java))
        //startService(Intent(this,LocationTrackingService::class.java))
    }

    override fun onStart() {
        super.onStart()
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect()
        }
    }

    override fun onStop() {
        super.onStop()
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect()
        }
    }
    override fun onConnectionSuspended(p0: Int) {

        Log.i(TAG, "Connection Suspended")
        mGoogleApiClient.connect()
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        Log.i(TAG, "Connection failed. Error: " + connectionResult.getErrorCode())
    }

    override fun onLocationChanged(location: Location) {
        var msg = "Updated Location: LatLon " + location.latitude + " - " + location.longitude
        //txt_latitude.setText(""+location.latitude)
        //txt_longitude.setText(""+location.longitude)
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        //var car:Car = getDefaultCar()
        var coordinates = Coordinates()
        coordinates.id = 0
        coordinates.car         = Car()
        coordinates.latitude    = location.latitude
        coordinates.longitude   = location.longitude
        callWebService(coordinates)
    }
/*
    private fun getDefaultCar(): Car {

        var driver:Driver = Driver("Jose","664764", 19880305)
        var car:Car = Car("cheve","2215563","azul",true,driver,EnumStatus.OFFLINE)
        return car
    }
*/
    private fun callWebService(coordinates: Coordinates) {
        doAsync {
            //val response = CoordinateService.save(coordinates)
            uiThread {

            }
        }
    }

    override fun onConnected(p0: Bundle?) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return
        }


        startLocationUpdates()

        var fusedLocationProviderClient :
                FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationProviderClient .getLastLocation()
                .addOnSuccessListener(this, OnSuccessListener<Location> { location ->
                    // Got last known location. In some rare situations this can be null.
                    if (location != null) {
                        // Logic to handle location object
                        mLocation = location;
                        //txt_latitude.setText("" + mLocation.latitude)
                        //txt_longitude.setText("" + mLocation.longitude)
                    }
                })
    }


    private fun checkLocation(): Boolean {
        if(!isLocationEnabled())
            showAlert()
        return isLocationEnabled()
    }

    private fun isLocationEnabled(): Boolean {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun showAlert() {
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle(this.getString(R.string.enableLocation))
                .setMessage(R.string.needEnableLocation)//"Precisamos ativar o GPS.\nPor favor ative-o."
                .setPositiveButton(R.string.enableGPS, DialogInterface.OnClickListener { paramDialogInterface, paramInt ->
                    val myIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    startActivity(myIntent)
                })
                .setNegativeButton(R.string.cancel, DialogInterface.OnClickListener { paramDialogInterface, paramInt -> })
        dialog.show()
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
}
