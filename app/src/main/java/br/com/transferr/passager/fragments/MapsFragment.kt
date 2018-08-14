package br.com.transferr.passager.fragments


import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Parcelable
import android.provider.Settings
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.transferr.fragments.SuperClassFragment
import br.com.transferr.passager.R
import br.com.transferr.passager.adapter.MapInfoWindowsAdapter
import br.com.transferr.passager.helpers.HelperCar
import br.com.transferr.passager.model.Quadrant
import br.com.transferr.passager.util.MyLocationLister
import br.com.transferr.passager.webservices.CarService
import br.com.transferr.util.PermissionUtil
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
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


/**
 * A simple [Fragment] subclass.
 */
class MapsFragment : SuperClassFragment(), OnMapReadyCallback,
GoogleApiClient.ConnectionCallbacks,
GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener,
        GoogleMap.OnMarkerClickListener{



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



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        locationManager = activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        mGoogleApiClient = GoogleApiClient.Builder(this!!.context!!)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build()
        startApi()
        mLocationManager = activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        checkLocation()

        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = activity?.supportFragmentManager
                ?.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    fun startApi(){
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect()
        }
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
        val dialog = AlertDialog.Builder(this!!.context!!)
        dialog.setTitle("Habilitar Localização")
                .setMessage("Precisamos ativar o GPS.\nPor favor ative-o.")
                .setPositiveButton("Ativar GPS", DialogInterface.OnClickListener { paramDialogInterface, paramInt ->
                    val myIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    startActivity(myIntent)
                })
                .setNegativeButton("Cancel", DialogInterface.OnClickListener { paramDialogInterface, paramInt -> })
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
        if(isMapAllowed()) {
            mMap.isMyLocationEnabled = true
        }else{
            activity?.toast("Acesso ao GPS negado. O aplicativo pode não funcionar corretamente.")
        }
        mMap.animateCamera(CameraUpdateFactory.zoomTo(ZOOM))
        mMap.setMaxZoomPreference(15f)
        mMap.setInfoWindowAdapter(MapInfoWindowsAdapter(this!!.context!!))
        mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
        //drawerLeftMenu()
    }

    private fun isMapAllowed():Boolean{
        return PermissionUtil.validate(this!!.activity!!,1,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION)
    }

    override fun onConnected(p0: Bundle?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onConnectionSuspended(p0: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onLocationChanged(location: Location?) {
        updateMapScreen(location)
    }

    private fun updateMapScreen(location: Location?){
        callWebService()
    }

    private fun callWebService() {
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
                        if (numCarFound == 0) {
                            numCarFound = markers.size
                        } else {
                            var size = markers.size
                            if (size != numCarFound) {
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

    override fun onMarkerClick(p0: Marker?): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
