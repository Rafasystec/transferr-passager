package br.com.transferr.passenger.activities

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import br.com.transferr.R
import br.com.transferr.passenger.adapter.DriversResponseAdapter
import br.com.transferr.passenger.extensions.defaultRecycleView
import br.com.transferr.passenger.extensions.showLoadingDialog
import br.com.transferr.passenger.interfaces.OnResponseInterface
import br.com.transferr.passenger.model.responses.ResponseDriver
import br.com.transferr.passenger.model.responses.ResponseDrivers
import br.com.transferr.passenger.model.responses.ResponseLocation
import br.com.transferr.passenger.webservices.WSDriver
import kotlinx.android.synthetic.passenger.layout_empty_list.*
import kotlinx.android.synthetic.passenger.layout_empty_list.view.*
import org.jetbrains.anko.*

class DriverListActivity : br.com.transferr.passenger.activities.SuperClassActivity() {

    private var recycleView: RecyclerView?=null
    private var idLocation = 0L
    val PERMISSION_TO_PHONE_CALL = 2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_driver_list)
        idLocation = intent.getLongExtra(ResponseLocation.LOCATION_PARAMETER_KEY,0L)
        recycleView = defaultRecycleView(this,R.id.rcDriversFromLocation)
        if(llEmptyList != null) {
            llEmptyList.tvTextToAdd.text = "Sem Motoristas no momento."
        }
        requestPermissionToPhoneCall()
    }

    override fun onResume() {
        super.onResume()
        loadDriversByLocation()
    }

    private fun loadDriversByLocation() {
        val dialog = showLoadingDialog()
        WSDriver.doGetByLocation(idLocation, object : OnResponseInterface<ResponseDrivers>{
            override fun onSuccess(body: ResponseDrivers?) {
                recycleView?.adapter = br.com.transferr.passenger.adapter.DriversResponseAdapter(body?.drivers!!, onClick = { driver: ResponseDriver -> cardViewOnClick(drivers = driver) })
                dialog.dismiss()
                checkIfIsEmpty(body)
            }

            override fun onError(message: String) {
                alertWarning(message)
                dialog.dismiss()
            }

            override fun onFailure(t: Throwable?) {
                alertErro(t?.message!!)
                dialog.dismiss()
            }

        })

    }

    private fun cardViewOnClick(drivers: ResponseDriver){
        toast("Selecionou o motorista ${drivers.name}")
    }

    fun checkIfIsEmpty(responseDrivers: ResponseDrivers){
        if(responseDrivers != null){
            val drivers = responseDrivers.drivers
            if(drivers?.isEmpty()!!){
                llEmptyList.visibility = View.VISIBLE
            }else{
                llEmptyList.visibility = View.GONE
            }
        }
    }

    private fun requestPermissionToPhoneCall(){
        if(ContextCompat.checkSelfPermission(this@DriverListActivity, Manifest.permission.CALL_PHONE) !=
                PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this@DriverListActivity,Manifest.permission.CALL_PHONE)){
                alert(R.string.needPermission,R.string.permissionToAccessLocation){
                    yesButton {

                    }
                    noButton {  }
                }.show()
            }else{
                ActivityCompat.requestPermissions(this@DriverListActivity,arrayOf(Manifest.permission.CALL_PHONE),PERMISSION_TO_PHONE_CALL)
            }

        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            PERMISSION_TO_PHONE_CALL ->{
                if (grantResults.isEmpty()
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
            }
        }
    }
}
