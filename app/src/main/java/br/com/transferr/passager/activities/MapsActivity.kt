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
import android.support.design.widget.NavigationView
import android.support.v4.app.ActivityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AlertDialog
import android.util.Log
import br.com.transferr.passager.R
import br.com.transferr.passager.adapter.MapInfoWindowsAdapter
import br.com.transferr.passager.helpers.HelperCar
import br.com.transferr.passager.model.Quadrant
import br.com.transferr.passager.util.MyLocationLister
import br.com.transferr.passager.webservices.CarService
import br.com.transferr.util.PermissionUtil
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.Marker
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread

class MapsActivity : SuperClassActivity(),
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        OnMapReadyCallback,com.google.android.gms.location.LocationListener,
        GoogleMap.OnMarkerClickListener{


    override fun onMarkerClick(p0: Marker?): Boolean {

        return true
    }


    private val TAG = "LOCATION"
    private lateinit var mGoogleApiClient: GoogleApiClient
    private lateinit var mMap: GoogleMap
    lateinit var locationManager: LocationManager
    private var mLocationManager: LocationManager? = null
    private var mLocationRequest: LocationRequest? = null
    private val UPDATE_INTERVAL = (2 * 1000).toLong()  /* 10 secs */
    private val FASTEST_INTERVAL: Long = 2000 /* 2 sec */
    private val ZOOM = 15f
    lateinit var mLocation: Location
    var numCarFound:Int = 0
    private lateinit var mDrawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        setContentView(R.layout.activity_maps)
        mGoogleApiClient = GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build()
        //addActionPrice()
        startApi()
        mLocationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        checkLocation()
        //drawerLeftMenu()
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }
/*
    private fun addActionPrice(){
        price.setOnClickListener { view ->
            Snackbar.make(view, "Custo médio do translado é R$ 20,00", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
    }
    */

    private fun drawerLeftMenu(){
        //----------------------------------------
        //Left Menu navigation
        //----------------------------------------
        //mDrawerLayout = findViewById(R.id.drawer_layout)
        mDrawerLayout  = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener {  menuItem ->
            // set item as selected to persist highlight
            menuItem.isChecked = true
            // close drawer when item is tapped
            //mDrawerLayout.closeDrawers()
            mDrawerLayout.closeDrawers()
            // Add code here to update the UI based on the item selected
            // For example, swap UI fragments here
            true
        }
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
        var mylocationListener = MyLocationLister()
        var myLocationLatLng = mylocationListener.getLocation(this.context)
        if(myLocationLatLng != null) {
            val update = CameraUpdateFactory.newLatLngZoom(myLocationLatLng, 16f)
            googleMap.moveCamera(update)
        }
        this.mMap = googleMap
        if(isMapAllowed()) {
            mMap.isMyLocationEnabled = true
        }else{
            toast("Acesso ao GPS negado. O aplicativo pode não funcionar corretamente.")
        }
        mMap.animateCamera(CameraUpdateFactory.zoomTo(ZOOM))
        mMap.setMaxZoomPreference(15f)
        mMap.setInfoWindowAdapter(MapInfoWindowsAdapter(this))
        mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
        //drawerLeftMenu()

    }

    private fun isMapAllowed():Boolean{
        return PermissionUtil.validate(this,1,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION)
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
            quadrant.farLeftLat   = visibleRegion.farLeft.latitude
            quadrant.farLeftLng   = visibleRegion.farLeft.longitude
            quadrant.farRightLat  = visibleRegion.farRight.latitude
            quadrant.farRightLng  = visibleRegion.farRight.longitude

            quadrant.nearLeftLat  = visibleRegion.nearLeft.latitude
            quadrant.nearLeftLng  = visibleRegion.nearLeft.longitude
            quadrant.nearRightLat = visibleRegion.nearRight.latitude
            quadrant.nearRightLng = visibleRegion.nearRight.longitude

            doAsync {
                var carOnlineList   = CarService().getCarOnline(quadrant)
                uiThread {
                    if(carOnlineList != null) {
                        var markers = HelperCar.transformInMarkers(carOnlineList)
                        if(numCarFound == 0){
                            numCarFound = markers.size
                        }else{
                            var size = markers.size
                            if(size != numCarFound){
                                numCarFound = size
                                //Snackbar.make(contex, "Replace with your own action", Snackbar.LENGTH_LONG)
                                        //.setAction("Action", null).show()
                            }
                        }

                        for (mark in markers) {
                            mMap.addMarker(mark)
                        }
                    }
                }
            }


        }


    }

    private fun updateMapScreen(location: Location?){
        callWebService()
    }

    override fun onLocationChanged(location: Location?) {
        updateMapScreen(location)
    }


    override fun onConnected(p0: Bundle?) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        startLocationUpdates()

        var fusedLocationProviderClient :
                FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationProviderClient .getLastLocation()
                .addOnSuccessListener(this, { location ->
                    if (location != null) {
                        mLocation = location
                    }
                })
    }

    override fun onConnectionSuspended(p0: Int) {
        Log.d(TAG, "Connection Suspended")
        mGoogleApiClient.connect()
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        Log.d("DEBUG","Failed on connection")
    }

    override fun onStart() {
        super.onStart()
        startApi()
    }

    fun startApi(){
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

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        for(result in grantResults){
           if(result == PackageManager.PERMISSION_GRANTED){
               mMap.isMyLocationEnabled = true
               return
           }
        }
    }




}
