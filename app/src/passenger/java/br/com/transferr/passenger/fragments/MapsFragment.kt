package br.com.transferr.passenger.fragments


import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.transferr.R
import br.com.transferr.fragments.SuperMapFragment
import br.com.transferr.main.util.GPSUtil
import br.com.transferr.main.util.PermissionUtil
import br.com.transferr.model.Quadrant
import br.com.transferr.passenger.activities.MapInfoWindowActivity
import br.com.transferr.passenger.extensions.fromJson
import br.com.transferr.passenger.helpers.HelperCar
import br.com.transferr.passenger.model.responses.ResponseCarsOnline
import br.com.transferr.passenger.util.MyLocationLister
import br.com.transferr.passenger.webservices.CarService
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import kotlinx.android.synthetic.passenger.fragment_maps.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread


/**
 * A simple [Fragment] subclass.
 */
class MapsFragment : SuperMapFragment(), OnMapReadyCallback
//        GoogleApiClient.ConnectionCallbacks,
//        GoogleApiClient.OnConnectionFailedListener,
//        com.google.android.gms.location.LocationListener

{

    private var googleApiclient: GoogleApiClient?=null
    private lateinit var mMap: GoogleMap
    private var gpsUtil:GPSUtil?=null
    private var latLng: LatLng? = null
    //private var mLocationManager: LocationManager? = null
    private val ZOOM = 15f
    var marker:Marker?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        mHandler = Handler()
        gpsUtil = GPSUtil(activity)
//        startRepeatingTask()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        //locationManager = activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        //mLocationManager = activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        checkLocation()
        setMainTitle(R.string.page_title_map)
        val view = inflater.inflate(R.layout.fragment_maps, container, false)
        //Start the map
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
//        buildMapsAPI()
        return view
    }





    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap?) {
        var mylocationListener = MyLocationLister()
        var myLocationLatLng = mylocationListener.getLocation(this.context!!)
        if(myLocationLatLng != null) {
            val update = CameraUpdateFactory.newLatLngZoom(myLocationLatLng, 16f)
            googleMap?.moveCamera(update)
        }
        this.mMap = googleMap!!
        checkPermissionToAccessLocation()
        if(isMapAllowed()) {
            mMap.isMyLocationEnabled = true
            mMap.uiSettings.isMyLocationButtonEnabled = false
        }else{
            activity?.toast("Acesso ao GPS negado. O aplicativo pode não funcionar corretamente.")
        }
        mMap.animateCamera(CameraUpdateFactory.zoomTo(ZOOM))
        mMap.setMaxZoomPreference(30f)
        mMap.setMinZoomPreference(14f)
        //mMap.setInfoWindowAdapter(br.com.transferr.passenger.adapter.MapInfoWindowsAdapter(this))
        mMap.setOnMarkerClickListener({marker->
            //updateCamera(marker.position!!)
            var car = fromJson<ResponseCarsOnline>(marker.snippet)
            activity?.startActivity<MapInfoWindowActivity>(ResponseCarsOnline.PARAM_CAR_OBJECT to car)
            true
        })
        mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
        callWebService()
        myLocationFab.setOnClickListener {
            if (PermissionUtil.hasPermission(activity!!, Manifest.permission.ACCESS_FINE_LOCATION)) run {
                latLng = gpsUtil?.location
                updateCamera(mMap!!, latLng)
            }
        }
        mMap.setOnCameraIdleListener {
            updateMapScreen()
        }
    }

    private fun updateMapScreen(){
        try {
            callWebService()
        }catch (e:Exception){
            Log.e("FATAL_ERRO","try to call car to show on map",e)
        }
    }
    private fun callWebService() {
//        if(mMap == null){
//            return
//        }
        val visibleRegion = mMap.projection.visibleRegion
        val quadrant = Quadrant()
        if (visibleRegion != null) {
            quadrant.farLeftLat = visibleRegion.farLeft.latitude
            quadrant.farLeftLng = visibleRegion.farLeft.longitude
            quadrant.farRightLat = visibleRegion.farRight.latitude
            quadrant.farRightLng = visibleRegion.farRight.longitude

            quadrant.nearLeftLat = visibleRegion.nearLeft.latitude
            quadrant.nearLeftLng = visibleRegion.nearLeft.longitude
            quadrant.nearRightLat = visibleRegion.nearRight.latitude
            quadrant.nearRightLng = visibleRegion.nearRight.longitude

            doAsync {
                val carOnlineList = CarService().getCarOnline(quadrant)
                uiThread {
                    if (carOnlineList != null) {
                        mMap.clear()
                        var markers = HelperCar.transformInMarkers(carOnlineList)
                        for (mark in markers) {
                            mMap.addMarker(mark)
                        }
                    }
                }
            }
        }
    }



//    private val mInterval = 10000L // 5 seconds by default, can be changed later
//    private var mHandler: Handler? = null
//
//    var mStatusChecker: Runnable = object : Runnable {
//        override fun run() {
//            try {
////                updateMapScreen()
//            } finally {
//                // 100% guarantee that this always happens, even if
//                // your update method throws an exception
//                mHandler?.postDelayed(this, mInterval)
//            }
//        }
//    }

//    fun startRepeatingTask() {
//        mStatusChecker.run()
//    }

//    fun stopRepeatingTask() {
//        mHandler?.removeCallbacks(mStatusChecker)
//    }

//    override fun onDestroy() {
//        super.onDestroy()
//        stopRepeatingTask()
//    }

//    internal fun updateCamera(latLng: LatLng?) {
//        if (latLng != null) {
//            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11f))
//        }
//    }

//    fun buildMapsAPI(){
//        googleApiclient = GoogleApiClient.Builder(activity!!)
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .addApi(LocationServices.API)
//                .build()
//        googleApiclient!!.connect()
//    }

//    override fun onConnected(p0: Bundle?) {
//        startLocationUpdates()
//    }
//
//    override fun onConnectionSuspended(p0: Int) {
//
//    }
//
//    override fun onConnectionFailed(p0: ConnectionResult) {
//
//    }
//    private var mLocationRequest: LocationRequest? = null
//    private val UPDATE_INTERVAL = (2 * 1000).toLong()  /* 10 secs */
//    private val FASTEST_INTERVAL: Long = 2000 /* 2 sec */
//    override fun onLocationChanged(location: Location?) {
//        updateMapScreen()
//    }

//    private fun startLocationUpdates() {
//
//        // Create the location request
//        mLocationRequest = LocationRequest.create()
//                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
//                .setInterval(UPDATE_INTERVAL)
//                .setFastestInterval(FASTEST_INTERVAL)
//        // Request location updates
//        if (ActivityCompat.checkSelfPermission(activity!!, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity!!, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            return
//        }
//        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiclient,
//                mLocationRequest, this)
//
//    }

}
