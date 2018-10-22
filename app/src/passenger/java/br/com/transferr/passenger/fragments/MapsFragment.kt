package br.com.transferr.passenger.fragments


import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.transferr.R
import br.com.transferr.fragments.SuperMapFragment
import br.com.transferr.model.Quadrant
import br.com.transferr.passenger.activities.MapInfoWindowActivity
import br.com.transferr.passenger.extensions.fromJson
import br.com.transferr.passenger.helpers.HelperCar
import br.com.transferr.passenger.model.responses.ResponseCarsOnline
import br.com.transferr.passenger.util.MyLocationLister
import br.com.transferr.passenger.webservices.CarService
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread


/**
 * A simple [Fragment] subclass.
 */
class MapsFragment : SuperMapFragment(), OnMapReadyCallback

{

    private lateinit var mMap: GoogleMap

    //private var mLocationManager: LocationManager? = null
    private val ZOOM = 15f
    var marker:Marker?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mHandler = Handler()
        startRepeatingTask()
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
        }else{
            activity?.toast("Acesso ao GPS negado. O aplicativo pode não funcionar corretamente.")
        }
        mMap.animateCamera(CameraUpdateFactory.zoomTo(ZOOM))
        mMap.setMaxZoomPreference(15f)
        //mMap.setInfoWindowAdapter(br.com.transferr.passenger.adapter.MapInfoWindowsAdapter(this))
        mMap.setOnMarkerClickListener({marker->
            //updateCamera(marker.position!!)
            var car = fromJson<ResponseCarsOnline>(marker.snippet)
            activity?.startActivity<MapInfoWindowActivity>(ResponseCarsOnline.PARAM_CAR_OBJECT to car)
            true
        })
        mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
    }



    private fun updateMapScreen(location: Location?){
        try {
            callWebService()
        }catch (e:Exception){
            Log.e("FATAL_ERRO","try to call car to show on map",e)
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
        if(mMap == null){
            return
        }
        var visibleRegion = mMap.projection.visibleRegion
        var quadrant = Quadrant()
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
                var carOnlineList = CarService().getCarOnline(quadrant)
                uiThread {
                    if (carOnlineList != null) {
                        var markers = HelperCar.transformInMarkers(carOnlineList)
                        for (mark in markers) {
                            mMap.addMarker(mark)
                        }
                    }
                }
            }
        }
    }



    private val mInterval = 10000L // 5 seconds by default, can be changed later
    private var mHandler: Handler? = null

    var mStatusChecker: Runnable = object : Runnable {
        override fun run() {
            try {
                updateMapScreen()
            } finally {
                // 100% guarantee that this always happens, even if
                // your update method throws an exception
                mHandler?.postDelayed(this, mInterval)
            }
        }
    }

    fun startRepeatingTask() {
        mStatusChecker.run()
    }

    fun stopRepeatingTask() {
        mHandler?.removeCallbacks(mStatusChecker)
    }

    override fun onDestroy() {
        super.onDestroy()
        stopRepeatingTask()
    }

    internal fun updateCamera(latLng: LatLng?) {
        if (latLng != null) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11f))
        }


    }

}
