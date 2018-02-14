package br.com.transferr.passager.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.location.LocationManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.support.v7.app.AlertDialog
import br.com.transferr.passager.R
import br.com.transferr.util.PermissionUtil
import com.google.android.gms.maps.*

import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    lateinit var locationManager: LocationManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        setContentView(R.layout.activity_main)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
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
        //Cria o objeto longitude e latitude
        val locationCar = LatLng(-3.7318616,-38.5266704)
        //Posiciona o mapa na cordenada (zoom 13)
        var location = locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        val latLng = LatLng(location.latitude,location.longitude)
        val update = CameraUpdateFactory.newLatLngZoom(latLng,13f)
        mMap.moveCamera(update)

        //Marcar olocal no mapa
        mMap.addMarker(MarkerOptions()
                .title("Marcado aqui")
                .snippet("Alguma decricao")
                .position(locationCar)
        )
        //var circleOptions = CircleOptions()
        //        .center(latLng)
        //.radius(1000.toDouble()) // In meters

        // Get back the mutable Circle
        //mMap.addCircle(circleOptions)
        var mapa:MapView
        //Tipo do mapa
        mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
        val allowed = PermissionUtil.validate(this,1,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION)
        if(allowed) {
            mMap.isMyLocationEnabled = true
        }
        doAsync {
            while (true) {
                var update = updateMap()
                uiThread { mMap.moveCamera(update) }
                Thread.sleep(3*1000)
            }
        }
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

    @SuppressLint("MissingPermission")
    private fun updateMap() : CameraUpdate {

        var location = locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        val latLng = LatLng(location.latitude,location.longitude)

        return CameraUpdateFactory.newLatLngZoom(latLng,13f)


    }

    private fun callWebService(){

    }

}
