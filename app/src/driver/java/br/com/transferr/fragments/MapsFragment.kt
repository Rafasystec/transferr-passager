package br.com.transferr.fragments


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.util.Log
import android.view.*
import android.widget.Switch
import br.com.transferr.R
import br.com.transferr.extensions.fromJson
import br.com.transferr.extensions.log
import br.com.transferr.extensions.setupToolbar
import br.com.transferr.extensions.showLoadingDialog
import br.com.transferr.helpers.HelperPassengersOnline
import br.com.transferr.main.util.Prefes
import br.com.transferr.model.Car
import br.com.transferr.model.Quadrant
import br.com.transferr.model.enums.EnumStatus
import br.com.transferr.model.responses.OnResponseInterface
import br.com.transferr.model.responses.RequestCoordinatesUpdate
import br.com.transferr.model.responses.ResponseCarOnline
import br.com.transferr.model.responses.ResponseOK
import br.com.transferr.passenger.activities.MapInfoWindowActivity
import br.com.transferr.passenger.helpers.HelperCar
import br.com.transferr.passenger.model.responses.ResponseCarsOnline
import br.com.transferr.passenger.webservices.CarServiceMain
import br.com.transferr.services.LocationTrackingService
import br.com.transferr.util.NetworkUtil
import br.com.transferr.webservices.CarService
import br.com.transferr.webservices.CoordinatePassService
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.VisibleRegion
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread


/**
 * A simple [Fragment] subclass.
 */
class MapsFragment : SuperMapFragment(), OnMapReadyCallback,com.google.android.gms.location.LocationListener, LocationListener {
    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
       //Do nothing yet
        log("onStatusChanged - Provider: $provider")
    }

    override fun onProviderEnabled(provider: String?) {
        //Do nothing yet
        log("onProviderEnabled - Provider: $provider")
    }

    override fun onProviderDisabled(provider: String?) {
        log("onProviderDisabled - Provider: $provider")
    }

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    var swtOnOff:Switch?=null
    override fun onLocationChanged(location: Location?) {
        log("onLocationChanged")
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
        this.map = map
        fusedLocationClient.lastLocation
                .addOnSuccessListener { location : Location? ->
                    if(location != null) {
                        var latlon = LatLng(location?.latitude!!, location.longitude)
                        updateCameraOnLocationChange(latlon)
                    }
                }
        this.map!!.mapType = GoogleMap.MAP_TYPE_NORMAL
        this.map!!.setOnMarkerClickListener({marker->
            //updateCamera(marker.position!!)
            var car = fromJson<ResponseCarsOnline>(marker.snippet)
            activity?.startActivity<MapInfoWindowActivity>(ResponseCarsOnline.PARAM_CAR_OBJECT to car)
            true
        })
        startRepeatingTask()
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

    private fun callWebService(){
        var visibleRegion = map!!.projection.visibleRegion
        if(!visibleRegion.equals(null) && isConnected()){
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
            //swtOnOff!!.setOnCheckedChangeListener{buttonView, isChecked ->   onOff(isChecked)}
            swtOnOff!!.setOnClickListener { onOff() }
        }
        val checked = swtOnOff!!.isChecked
        changeSwitch(checked)
    }

    private fun changeSwitch(checked: Boolean) {
        if (checked) {
            swtOnOff!!.text = getString(R.string.online)+"      "
            swtOnOff!!.setTextColor(resources.getColor(R.color.green))
        } else {
            swtOnOff!!.setTextColor(resources.getColor(R.color.red))
            swtOnOff!!.text = getString(R.string.offline) +"      "
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when(item?.itemId){
            R.id.menu_switch_online ->{
                onOff()
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

    private fun onOff(){
        if(swtOnOff != null) {
            var isChecked: Boolean = swtOnOff?.isChecked!!
            changeSwitch(isChecked)
            stopInitLocation(isChecked)
            callWebServiceToMarck()
        }
    }

    private fun stopInitLocation(isChecked:Boolean){
        if(isChecked){
            startService()
        }else{
            stopServiceIntent()
        }
        onlineOffline(isChecked)
        updateStatus(isChecked,Prefes.prefsCar)
    }

    private fun startService(){
        if(checkLocation()) {
            startServiceIntent()
        }
        //activity?.startService(Intent(activity, LocationTrackingService::class.java))
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

    fun updateStatus(isChecked: Boolean, idCar:Long){
        var response = ResponseCarOnline()
        if(isChecked){
            response.status = EnumStatus.WAITING_CLIENT_TRANSFER
        }else{
            response.status = EnumStatus.OFFLINE
        }
        response.idCar = idCar

        CarService.updateStatus(response, object : OnResponseInterface<ResponseOK>{
            var dialog = showLoadingDialog()
            override fun onSuccess(body: ResponseOK?) {
                dialog.dismiss()
            }

            override fun onError(message: String) {
                dialog.dismiss()
            }

            override fun onFailure(t: Throwable?) {
                dialog.dismiss()
            }

        })
    }

    fun updateSwitch(car:Car){
        //var car = Prefes.prefsCarJSON
        swtOnOff?.isChecked = car.status != EnumStatus.OFFLINE
        onOff()
    }

    override fun onResume() {
        super.onResume()
        getCurrentCar()
    }

    private fun getCurrentCar(){
        CarService.getCar(Prefes.prefsCar, object :OnResponseInterface<Car>{
            var dialog = showLoadingDialog()
            override fun onSuccess(car: Car?) {
                updateSwitch(car!!)
                dialog.dismiss()
            }

            override fun onError(message: String) {
                dialog.dismiss()
            }

            override fun onFailure(t: Throwable?) {
                dialog.dismiss()
            }

        })
    }

    private val mInterval = 10000L // 5 seconds by default, can be changed later
    private var mHandler: Handler? = Handler()
   private var mStatusChecker: Runnable = object : Runnable {
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

    private fun startRepeatingTask() {
        mStatusChecker.run()
    }

    private fun stopRepeatingTask() {
        mHandler?.removeCallbacks(mStatusChecker)
    }

    private fun updateMapScreen(){
        try {
            callWebServiceToMarck()
        }catch (e:Exception){
            Log.e("FATAL_ERRO","try to call car to show on map",e)
        }
    }
    private fun callWebServiceToMarck() {
        if(map == null){
            return
        }
        var visibleRegion = map!!.projection.visibleRegion
        var quadrant = Quadrant()
        if (!visibleRegion.equals(null)) {
            quadrant.farLeftLat     = visibleRegion.farLeft.latitude
            quadrant.farLeftLng     = visibleRegion.farLeft.longitude
            quadrant.farRightLat    = visibleRegion.farRight.latitude
            quadrant.farRightLng    = visibleRegion.farRight.longitude
            quadrant.nearLeftLat    = visibleRegion.nearLeft.latitude
            quadrant.nearLeftLng    = visibleRegion.nearLeft.longitude
            quadrant.nearRightLat   = visibleRegion.nearRight.latitude
            quadrant.nearRightLng   = visibleRegion.nearRight.longitude
            doAsync {
                var carOnlineList = CarServiceMain().getCarOnline(quadrant)
                uiThread {
                    map!!.clear()
                    if (carOnlineList != null) {
                        var markers = HelperCar.transformInMarkers(carOnlineList)
                        for (mark in markers) {
                            map!!.addMarker(mark)
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopRepeatingTask()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getCurrentCar()
    }


}// Required empty public constructor
