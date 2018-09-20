package br.com.transferr.passenger.fragments


import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.support.design.widget.NavigationView
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityCompat.checkSelfPermission
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.transferr.R
import br.com.transferr.fragments.SuperClassFragment
import br.com.transferr.passenger.adapter.MapInfoWindowsAdapter
import br.com.transferr.passenger.helpers.HelperCar
import br.com.transferr.passenger.model.Quadrant
import br.com.transferr.passenger.util.MyLocationLister
import br.com.transferr.passenger.webservices.CarService
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
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import org.jetbrains.anko.*


/**
 * A simple [Fragment] subclass.
 */
class MapsFragment : SuperClassFragment(), OnMapReadyCallback

{

    private lateinit var mMap: GoogleMap
    lateinit var locationManager: LocationManager
    private var mLocationManager: LocationManager? = null
    private val ZOOM = 15f
    var marker:Marker?=null

    val PERMISSION_TO_ACCESS_LOCATION = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mHandler = Handler()
        startRepeatingTask()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        locationManager = activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        mLocationManager = activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        checkLocation()
        setMainTitle(R.string.page_title_map)
        val view = inflater.inflate(R.layout.fragment_maps, container, false)
        //Start the map
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
        return view
    }

    private fun checkLocation(): Boolean {
        if(!isLocationEnabled())
            showAlert()
        return isLocationEnabled()
    }

    private fun isLocationEnabled(): Boolean {
        locationManager = activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun showAlert() {
        val dialog = AlertDialog.Builder(this!!.activity!!)
        dialog.setTitle("Habilitar Localização")
                .setMessage("Precisamos ativar o GPS.\nPor favor ative-o.")
                .setPositiveButton("Ativar GPS", DialogInterface.OnClickListener { paramDialogInterface, paramInt ->
                    val myIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    startActivity(myIntent)
                })
                .setNegativeButton("Cancel", null)
        dialog.show()
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
        mMap.setInfoWindowAdapter(br.com.transferr.passenger.adapter.MapInfoWindowsAdapter(this))
        //mMap.setOnMarkerClickListener({marker->
        //    //updateCamera(marker.position!!)
        //    marker.showInfoWindow()
        //    //marker.hideInfoWindow()
        //    true
        //})

        mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
        //drawerLeftMenu()
    }

    private fun isMapAllowed():Boolean{
        return PermissionUtil.requestPermission(this!!.activity!!,1,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION)
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

    private fun checkPermissionToAccessLocation(){
        if(ContextCompat.checkSelfPermission(activity!!,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            //Should We show an explanation
            if(ActivityCompat.shouldShowRequestPermissionRationale(activity!!,Manifest.permission.ACCESS_FINE_LOCATION)){
                activity!!.alert(R.string.needPermission,R.string.permissionToAccessLocation){
                    yesButton {

                    }
                    noButton {  }
                }.show()
            }else{
                ActivityCompat.requestPermissions(activity!!, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),PERMISSION_TO_ACCESS_LOCATION)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            PERMISSION_TO_ACCESS_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty()
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    activity?.toast("Permissão concedida")
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    activity?.toast("Permissão negada")
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
