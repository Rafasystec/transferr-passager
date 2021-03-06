package br.com.transferr.fragments


import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.*
import android.widget.Switch
import br.com.transferr.R
import br.com.transferr.application.ApplicationTransferr
import br.com.transferr.extensions.*
import br.com.transferr.helpers.HelperPassengersOnline
import br.com.transferr.main.util.GPSUtil
import br.com.transferr.main.util.PermissionUtil
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
import kotlinx.android.synthetic.driver.fragment_maps.*
import org.jetbrains.anko.*


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
    private var car :Car?=null
    private var gpsUtil: GPSUtil?=null//(ApplicationTransferr.getInstance().applicationContext)
    private var latLng: LatLng? = null
    override fun onMapReady(map: GoogleMap) {
        this.map = map
        if(!checkFineLocationPermission()) {
            checkPermissionToAccessLocation()
        }else {
            initMapWhenItReady()
        }
    }

    private fun checkFineLocationPermission() =
            ContextCompat.checkSelfPermission(activity!!, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED

    @SuppressLint("MissingPermission")
    private fun initMapWhenItReady() {
        locationManager!!.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, this)

        if (isMapAllowed()) {
            this.map?.isMyLocationEnabled = false
            this.map?.uiSettings?.isMyLocationButtonEnabled = true
        } else {
            activity?.toast("Acesso ao GPS negado. O aplicativo pode não funcionar corretamente.")
        }

        map?.setMaxZoomPreference(30f)
        map?.setMinZoomPreference(14f)
        //this.map = map
        fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        var latlon = LatLng(location?.latitude!!, location.longitude)
                        updateCameraOnLocationChange(latlon)
                    }
                }
        this.map!!.mapType = GoogleMap.MAP_TYPE_NORMAL
        this.map!!.setOnMarkerClickListener({ marker ->
            //updateCamera(marker.position!!)
            var car = fromJson<ResponseCarsOnline>(marker.snippet)
            activity?.startActivity<MapInfoWindowActivity>(ResponseCarsOnline.PARAM_CAR_OBJECT to car)
            true
        })
        startRepeatingTask()
        startService()
        setOnCameraIdleListener(map!!)
        callWebServiceToMark(map!!,true)
        myLocationFab.setOnClickListener {
            if (PermissionUtil.hasPermission(activity!!, Manifest.permission.ACCESS_FINE_LOCATION)) run {
                latLng = gpsUtil?.location
                updateCamera(this.map!!, latLng)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

            when(requestCode){
                PERMISSION_TO_ACCESS_LOCATION -> {
                    // If request is cancelled, the result arrays are empty.
                    if (grantResults.isNotEmpty()
                            && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                        // permission was granted, yay! Do the
                        // contacts-related task you need to do.
                        activity?.toast(getString(R.string.permissionGranted))
                        initMapWhenItReady()
                    } else {

                        // permission denied, boo! Disable the
                        // functionality that depends on this permission.
                        activity?.toast(getString(R.string.permissionDied))
                    }
                }
            }

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
        gpsUtil = GPSUtil(activity)
        return view
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
        //val checked = swtOnOff!!.isChecked
        //changeSwitch(checked)
        changeSwitch(this.car?.status  != EnumStatus.OFFLINE)
    }

    private fun changeSwitch(checked: Boolean) {
        swtOnOff?.isChecked = checked
        if (checked) {
            swtOnOff?.text = getString(R.string.online)+"      "
            swtOnOff?.setTextColor(resources.getColor(R.color.green))

        } else {
            swtOnOff?.setTextColor(resources.getColor(R.color.red))
            swtOnOff?.text = getString(R.string.offline) +"      "
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

                    }
            ,activity,progress)
        }else{
            CarService.offline(request,
                    object : OnResponseInterface<ResponseOK> {
                        override fun onSuccess(body: ResponseOK?) {
                            progress.dismiss()
                        }

                    }
            )
        }
    }

    private fun onOff(){
        if(swtOnOff != null) {
            var isChecked: Boolean = swtOnOff?.isChecked!!
            if(!isChecked && this.car?.alwaysOnMap!!){
                activity?.alert (message = getString(R.string.alwaysParamIsTrueMessage)){
                    okButton { putCarOnMap(isChecked) }
                    onCancelled { putCarOnMap(isChecked) }
                }?.show()
            }else{
                putCarOnMap(isChecked)
            }
        }
    }

    fun putCarOnMap(isChecked:Boolean){
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
        updateStatus(isChecked,Prefes.prefsCar)
    }

    private fun startService(){
        if(checkFineLocationPermission()) {
            if (isLocationEnabled()) {
                startServiceIntent()
            }
        }
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
        })
    }

    fun updateSwitch(car:Car){
        swtOnOff?.isChecked = car.status != EnumStatus.OFFLINE
        onOff()
    }

    private fun getCurrentCar(){
        var dialog = showLoadingDialog()
        CarService.getCar(Prefes.prefsCar, object :OnResponseInterface<Car>{
            override fun onSuccess(carParam: Car?) {
                try {
                    car = carParam
                    updateSwitch(carParam!!)
                }finally {
                    dialog.dismiss()
                }

            }
        },activity,dialog)
    }

    private val mInterval = 20000L // 5 seconds by default, can be changed later
    private var mHandler: Handler? = Handler()
   private var mStatusChecker: Runnable = object : Runnable {
        override fun run() {
            try {
                updateMapScreen()
            } finally {
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
            callWebServiceToMark(map!!,true)
        }catch (e:Exception){
            Log.e("FATAL_ERRO","try to call car to show on map",e)
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

    override fun onResume() {
        super.onResume()
    }


}// Required empty public constructor
