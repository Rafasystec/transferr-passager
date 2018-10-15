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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import br.com.transferr.R
import br.com.transferr.main.util.PermissionUtil
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import org.jetbrains.anko.toast
import org.jetbrains.anko.yesButton


/**
 * A simple [Fragment] subclass.
 * Author Rafael Rocha on 11/10/2018
 */
open class SuperMapFragment : SuperClassFragment() {

    val PERMISSION_TO_ACCESS_LOCATION = 1
    lateinit var locationManager: LocationManager

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


}// Required empty public constructor
