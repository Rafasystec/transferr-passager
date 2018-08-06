package br.com.transferr.passager.activities

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.View
import br.com.transferr.passager.R
import br.com.transferr.passager.adapter.DriversResponseAdapter
import br.com.transferr.passager.extensions.defaultRecycleView
import br.com.transferr.passager.interfaces.OnResponseInterface
import br.com.transferr.passager.model.responses.ResponseDriver
import br.com.transferr.passager.model.responses.ResponseDrivers
import br.com.transferr.passager.model.responses.ResponseLocation
import br.com.transferr.passager.webservices.WSDriver
import kotlinx.android.synthetic.main.layout_empty_list.*
import kotlinx.android.synthetic.main.layout_empty_list.view.*
import org.jetbrains.anko.progressDialog
import org.jetbrains.anko.toast

class DriverListActivity : SuperClassActivity() {

    private var recycleView: RecyclerView?=null
    private var idLocation = 0L;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_driver_list)
        idLocation = intent.getLongExtra(ResponseLocation.LOCATION_PARAMETER_KEY,0L)
        recycleView = defaultRecycleView(this,R.id.rcDriversFromLocation)
        if(llEmptyList != null) {
            llEmptyList.tvTextToAdd.text = "Sem Motoristas no momento."
        }
    }

    override fun onResume() {
        super.onResume()
        loadDriversByLocation()
    }

    private fun loadDriversByLocation() {
        val dialog = progressDialog(message = R.string.loading, title = R.string.wait)
        WSDriver.doGetByLocation(idLocation, object : OnResponseInterface<ResponseDrivers>{
            override fun onSuccess(body: ResponseDrivers?) {
                recycleView?.adapter = DriversResponseAdapter(body?.drivers!!,onClick = { driver:ResponseDriver -> cardViewOnClick(drivers = driver)})
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
}
