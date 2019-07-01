package br.com.transferr.fragments


import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.support.annotation.StringRes
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import br.com.transferr.R
import br.com.transferr.main.util.PermissionUtil
import br.com.transferr.model.Quadrant
import br.com.transferr.passenger.helpers.HelperCar
import br.com.transferr.passenger.model.responses.ResponseCarsOnline
import br.com.transferr.passenger.webservices.CarServiceMain
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import org.jetbrains.anko.*


/**
 * A simple [Fragment] subclass.
 * Author Rafael Rocha on 11/10/2018
 */
open class SuperMapFragment : SuperClassFragment() {

    val PERMISSION_TO_ACCESS_LOCATION = 1
    //var isWaitingResponse = false
    lateinit var locationManager: LocationManager
    var tempListOfOnlineCars: MutableList<ResponseCarsOnline> = mutableListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val textView = TextView(activity)
        textView.setText(R.string.hello_blank_fragment)
        return textView
    }

    protected fun checkPermissionToAccessLocation(){
        if(ContextCompat.checkSelfPermission(activity!!, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            //Should We show an explanation
            if(ActivityCompat.shouldShowRequestPermissionRationale(activity!!,Manifest.permission.ACCESS_FINE_LOCATION)){
                activity!!.alert(title =  getString(R.string.needPermission) , message = getString(R.string.permissionToAccessLocation) ){
                    yesButton {
                        //ActivityCompat.requestPermissions(activity!!, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),PERMISSION_TO_ACCESS_LOCATION)
                        requestLocationPermission()
                    }
                    noButton {  }
                }.show()
            }else{
                //ActivityCompat.requestPermissions(activity!!, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),PERMISSION_TO_ACCESS_LOCATION)
                requestLocationPermission()
            }
        }
    }

    private fun requestLocationPermission() {
        requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION_TO_ACCESS_LOCATION)
    }


    protected fun isMapAllowed():Boolean{
        return PermissionUtil.requestPermission(this!!.activity!!,1,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION)
    }

    protected fun checkLocation(): Boolean {
        if(!isLocationEnabled())
            showAlert()
        return isLocationEnabled()
    }

    protected fun isLocationEnabled(): Boolean {
        locationManager = activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun showAlert() {
        val dialog = AlertDialog.Builder(this!!.activity!!)
        dialog.setTitle(R.string.enableLocation)
                .setMessage(R.string.needEnableLocation)
                .setPositiveButton(R.string.enableGPS, DialogInterface.OnClickListener { paramDialogInterface, paramInt ->
                    val myIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    startActivity(myIntent)
                })
                .setNegativeButton(R.string.cancel, null)
        dialog.show()
    }

    protected fun setOnCameraIdleListener(map: GoogleMap){
        map.setOnCameraIdleListener {
            callWebServiceToMark(map,false)
        }
    }

    protected fun callWebServiceToMark(map: GoogleMap, clearMap:Boolean) {
        if(map == null){
            return
        }
        //if(isWaitingResponse){
        //    return
        //}
        var visibleRegion = map!!.projection.visibleRegion
        var quadrant = Quadrant()
        if (visibleRegion != null) {
            quadrant.farLeftLat     = visibleRegion.farLeft.latitude
            quadrant.farLeftLng     = visibleRegion.farLeft.longitude
            quadrant.farRightLat    = visibleRegion.farRight.latitude
            quadrant.farRightLng    = visibleRegion.farRight.longitude
            quadrant.nearLeftLat    = visibleRegion.nearLeft.latitude
            quadrant.nearLeftLng    = visibleRegion.nearLeft.longitude
            quadrant.nearRightLat   = visibleRegion.nearRight.latitude
            quadrant.nearRightLng   = visibleRegion.nearRight.longitude
            doAsync {
                try {
                    //isWaitingResponse = true
                    var carOnlineList = CarServiceMain().getCarOnline(quadrant)
                    uiThread {
                        //isWaitingResponse = false
                        if (clearMap) {
                            map!!.clear()
                        }
                        if (carOnlineList != null) {

                            if(clearMap){
                                var markers = HelperCar.transformInMarkers(carOnlineList)
                                for (mark in markers) {
                                    map!!.addMarker(mark)
                                }
                                tempListOfOnlineCars = carOnlineList as MutableList<ResponseCarsOnline>
                            }else{
                                if(tempListOfOnlineCars != null){
                                    carOnlineList
                                            .filter { tempListOfOnlineCars.contains(it) }
                                            .forEach { carOnlineList.remove(it) }
                                    var markers = HelperCar.transformInMarkers(carOnlineList)
                                    for (mark in markers) {
                                        map!!.addMarker(mark)
                                    }
                                }
                            }

                        }
                    }
                }catch (exception : Exception){
                    //isWaitingResponse = false
                }
            }
        }
    }

    internal fun updateCamera(map: GoogleMap,latLng: LatLng?) {
        if (latLng != null)
            map.animateCamera(CameraUpdateFactory.newLatLng(latLng))

    }


}// Required empty public constructor
