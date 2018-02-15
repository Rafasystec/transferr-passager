package br.com.transferr.passager.activities

import android.Manifest
import android.annotation.SuppressLint
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
import android.support.v7.app.AppCompatActivity
import android.util.Log
import br.com.transferr.passager.R
import br.com.transferr.passager.helpers.HelperCar
import br.com.transferr.passager.model.Quadrant
import br.com.transferr.passager.webservices.CarService
import br.com.transferr.util.PermissionUtil
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.OnSuccessListener
import org.jetbrains.anko.toast

class MainActivity : AppCompatActivity(), GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback,com.google.android.gms.location.LocationListener {


    private val TAG = "LOCATION"
    private lateinit var mGoogleApiClient: GoogleApiClient
    private lateinit var mMap: GoogleMap
    lateinit var locationManager: LocationManager
    private var mLocationManager: LocationManager? = null
    private var mLocationRequest: LocationRequest? = null
    private val UPDATE_INTERVAL = (2 * 1000).toLong()  /* 10 secs */
    private val FASTEST_INTERVAL: Long = 2000 /* 2 sec */
    lateinit var mLocation: Location
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        setContentView(R.layout.activity_main)
        mGoogleApiClient = GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build()

        mLocationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        checkLocation()
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        this.mMap = googleMap
        mMap.setMaxZoomPreference(15f)
        mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
        val allowed = PermissionUtil.validate(this,1,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION)
        if(allowed) {
            mMap.isMyLocationEnabled = true
        }
        /*
        doAsync {
            while (true) {
                var update = updateMap()
                uiThread { mMap.moveCamera(update) }
                Thread.sleep(3*1000)
            }
        }
        */
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
        dialog.setTitle("Habilitar Localização")
                .setMessage("Precisamos ativar o GPS.\nPor favor ative-o.")
                .setPositiveButton("Ativar GPS", DialogInterface.OnClickListener { paramDialogInterface, paramInt ->
                    val myIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    startActivity(myIntent)
                })
                .setNegativeButton("Cancel", DialogInterface.OnClickListener { paramDialogInterface, paramInt -> })
        dialog.show()
    }


    private fun callWebService(){
        var visibleRegion = mMap.projection.visibleRegion
        var quadrant      = Quadrant()
        if(visibleRegion != null){
            var farLeft   = visibleRegion.farLeft
            var farRight  = visibleRegion.farRight
            var nearLeft  = visibleRegion.nearLeft
            var nearRight = visibleRegion.nearRight
            quadrant.nearRight  = nearRight
            quadrant.nearLeft   = nearLeft
            quadrant.farRight   = farRight
            quadrant.farLeft    = farLeft
            var carOnlineList   = CarService().getCarOnline(quadrant)
            var marKers         = HelperCar.transformInMarkers(carOnlineList)
            for (mark in marKers){
                mMap.addMarker(mark)
            }
        }


    }

    private fun updateMapScreen(location: Location?){
        //var location = locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        val latLng = LatLng(location?.latitude!!,location?.longitude)
        val update = CameraUpdateFactory.newLatLngZoom(latLng,13f)

        var marker1:MarkerOptions = MarkerOptions().position(LatLng(-3.73682921,-38.48748711))
                .title("Bem no PAPICU")
                .snippet("Ok, this is my snippet")

        mMap.addMarker(marker1)
        callWebService()
        mMap.moveCamera(update)
    }


    override fun onLocationChanged(location: Location?) {
        updateMapScreen(location)
        toast("Minha localização mudou")
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
                        mLocation = location
                        //txt_latitude.setText("" + mLocation.latitude)
                        //txt_longitude.setText("" + mLocation.longitude)
                    }
                })
    }

    override fun onConnectionSuspended(p0: Int) {
        Log.d(TAG, "Connection Suspended")
        mGoogleApiClient.connect()
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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

    private fun startLocationUpdates() {

        // Create the location request
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL)
        // Request location updates
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest, this)

    }

}
