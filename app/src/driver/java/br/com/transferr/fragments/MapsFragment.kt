package br.com.transferr.fragments


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import android.widget.Switch
import br.com.transferr.R
import br.com.transferr.extensions.log
import br.com.transferr.extensions.setupToolbar
import br.com.transferr.extensions.showAlert
import br.com.transferr.extensions.showLoadingDialog
import br.com.transferr.helpers.HelperPassengersOnline
import br.com.transferr.main.util.Prefes
import br.com.transferr.model.Quadrant
import br.com.transferr.model.responses.OnResponseInterface
import br.com.transferr.model.responses.RequestCoordinatesUpdate
import br.com.transferr.model.responses.ResponseOK
import br.com.transferr.services.LocationTrackingService
import br.com.transferr.util.MyLocationLister
import br.com.transferr.util.NetworkUtil
import br.com.transferr.webservices.CarService
import br.com.transferr.webservices.CoordinatePassService
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.VisibleRegion
import kotlinx.android.synthetic.driver.activity_main.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread


/**
 * A simple [Fragment] subclass.
 */
class MapsFragment : SuperMapFragment(), OnMapReadyCallback,com.google.android.gms.location.LocationListener, LocationListener {
    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
       //Do nothing yet
    }

    override fun onProviderEnabled(provider: String?) {
        //Do nothing yet
    }

    override fun onProviderDisabled(provider: String?) {
        //Do nothing yet
    }

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    var swtOnOff:Switch?=null
    override fun onLocationChanged(location: Location?) {
        if (location != null) {
            map?.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(location.latitude, location.longitude), 13f))

            val cameraPosition = CameraPosition.Builder()
                    .target(LatLng(location.latitude, location.longitude))      // Sets the center of the map to location user
                    .zoom(13f)                   // Sets the zoom
                    .bearing(90f)                // Sets the orientation of the camera to east
                    .tilt(40f)                   // Sets the tilt of the camera to 30 degrees
                    .build()                   // Creates a CameraPosition from the builder
            map?.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
        }
    }

    private var map : GoogleMap? = null

    @SuppressLint("MissingPermission")
    override fun onMapReady(map: GoogleMap) {
        checkPermissionToAccessLocation()
        locationManager!!.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, this)
        if(isMapAllowed()) {
            map.isMyLocationEnabled = true
        }else{
            activity?.toast("Acesso ao GPS negado. O aplicativo pode não funcionar corretamente.")
        }
        map.setMaxZoomPreference(16f)
        map.setMinZoomPreference(12f)
        map.setOnCameraMoveListener {
            log("Vc moveu o mapa")
            callWebService()
        }
        this.map = map
        fusedLocationClient.lastLocation
                .addOnSuccessListener { location : Location? ->
                    if(location != null) {
                        var latlon = LatLng(location?.latitude!!, location.longitude)
                        updateCameraOnLocationChange(latlon)
                    }
                }
        //var mylocationListener = MyLocationLister()
        //var myLocationLatLng = mylocationListener.getLocation(this.context!!)

        map.mapType = GoogleMap.MAP_TYPE_NORMAL
        callWebService()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_maps, container, false)
        setupToolbar(R.id.toolbar,getString(R.string.Map))
        checkLocation()
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
        setHasOptionsMenu(true)
        locationManager = context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity!!)
        return view
    }
    /*
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater?.inflate(R.layout.fragment_maps, container, false)
        //Inicia o map
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
        return view
    }
    */

    private fun callWebService(){
        var visibleRegion = map!!.projection.visibleRegion

        if(visibleRegion != null && isConnected()){
            doAsync {

                var carOnlineList   = CoordinatePassService
                                            .getOnlinePassengers(getQuadrantByVisibleRegioan(visibleRegion))
                uiThread {
                    if(carOnlineList != null) {
                        var markers = HelperPassengersOnline.transformInMarkers(carOnlineList)
                        for (mark in markers) {
                            map!!.addMarker(mark)
                        }
                    }
                }
            }
        }
    }

    private fun getQuadrantByVisibleRegioan(visibleRegion: VisibleRegion):Quadrant{
        var quadrant      = Quadrant()
        quadrant.farLeftLat   = visibleRegion.farLeft.latitude
        quadrant.farLeftLng   = visibleRegion.farLeft.longitude
        quadrant.farRightLat  = visibleRegion.farRight.latitude
        quadrant.farRightLng  = visibleRegion.farRight.longitude

        quadrant.nearLeftLat  = visibleRegion.nearLeft.latitude
        quadrant.nearLeftLng  = visibleRegion.nearLeft.longitude
        quadrant.nearRightLat = visibleRegion.nearRight.latitude
        quadrant.nearRightLng = visibleRegion.nearRight.longitude
        return quadrant
    }

    private fun isConnected():Boolean{
        return NetworkUtil.isNetworkAvailable(this.context!!)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.menu_switch,menu)
        val item = menu?.findItem(R.id.menu_switch_online)
        item?.setActionView(R.layout.switch_online_offline)
        swtOnOff = item?.actionView?.findViewById<Switch>(R.id.swtOnlineOffline) as Switch
        if(swtOnOff != null){
            swtOnOff!!.setOnCheckedChangeListener{buttonView, isChecked ->   onOff(isChecked)}
        }
        val checked = swtOnOff!!.isChecked
        changeSwitch(checked)
    }

    private fun changeSwitch(checked: Boolean) {
        if (checked) {
            swtOnOff!!.setText(R.string.online)
            swtOnOff!!.setTextColor(resources.getColor(R.color.green))
        } else {
            swtOnOff!!.setTextColor(resources.getColor(R.color.red))
            swtOnOff!!.setText(R.string.offline)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when(item?.itemId){
            R.id.menu_switch_online ->{
                onOff(item.isChecked)
                true
            }else->super.onOptionsItemSelected(item)
        }
    }

    private fun onlineOffline(online:Boolean){
        var request = RequestCoordinatesUpdate()
        request.idCar = Prefes.prefsCar
        var progress = showLoadingDialog()
        if(online) {
            CarService.online(request,
                    object : OnResponseInterface<ResponseOK> {
                        override fun onSuccess(body: ResponseOK?) {
                            progress.dismiss()
                        }

                        override fun onError(message: String) {
                            progress.dismiss()
                        }

                        override fun onFailure(t: Throwable?) {
                            progress.dismiss()
                        }

                    }
            )
        }else{
            CarService.offline(request,
                    object : OnResponseInterface<ResponseOK> {
                        override fun onSuccess(body: ResponseOK?) {
                            progress.dismiss()
                        }

                        override fun onError(message: String) {
                            progress.dismiss()
                        }

                        override fun onFailure(t: Throwable?) {
                            progress.dismiss()
                        }

                    }
            )
        }

    }

    private fun onOff(isChecked: Boolean){
        changeSwitch(isChecked)
        stopInitLocation(isChecked)
    }

    private fun stopInitLocation(isChecked:Boolean){
        if(isChecked){
            startService()
        }else{
            stopServiceIntent()
        }
        onlineOffline(isChecked)
    }

    private fun startService(){
        if(checkLocation()) {
            startServiceIntent()
        }
        activity?.startService(Intent(activity, LocationTrackingService::class.java))
    }

    private fun stopServiceIntent(){
        activity?.stopService(Intent(activity, LocationTrackingService::class.java))
    }

    private fun startServiceIntent(){
        activity?.startService(Intent(activity, LocationTrackingService::class.java))
    }

    fun updateCameraOnLocationChange(location:LatLng?){
        if(location != null) {
            val update = CameraUpdateFactory.newLatLngZoom(location, 16f)
            map?.moveCamera(update)
        }
    }

}// Required empty public constructor
