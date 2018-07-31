package br.com.transferr.passager.activities

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import br.com.transferr.passager.R
import br.com.transferr.passager.adapter.DriversResponseAdapter
import br.com.transferr.passager.extensions.defaultRecycleView
import br.com.transferr.passager.interfaces.OnResponseInterface
import br.com.transferr.passager.model.responses.ResponseDriver
import br.com.transferr.passager.model.responses.ResponseDrivers
import br.com.transferr.passager.model.responses.ResponseLocation
import br.com.transferr.passager.webservices.WSDriver
import org.jetbrains.anko.toast

class DriverListActivity : SuperClassActivity() {

    private var recycleView: RecyclerView?=null
    private var idLocation = 0L;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_driver_list)
        idLocation = intent.getLongExtra(ResponseLocation.LOCATION_PARAMETER_KEY,0L)
        recycleView = defaultRecycleView(this,R.id.rcDriversFromLocation)

    }

    override fun onResume() {
        super.onResume()
        loadDriversByLocation()
    }

    private fun loadDriversByLocation() {
        /*
        var driverList = ResponseDrivers()
        var driver = ResponseDriver()
        with (driver) {
            name = "Assunção de almeida"
            email = "assdealm@gmail.com"
            birthDate = "05/04/1989"
            countryRegister = "UHD-7052"

        }
        var driver1 = ResponseDriver()
        with (driver1) {
            name = "Avelino Aldeia"
            email = "avelinadd28399@gmail.com"
            birthDate = "05/04/1978"
            countryRegister = "PWS-0221"
        }

        driverList.drivers = listOf(driver,driver1)
        */
        WSDriver.doGetByLocation(idLocation, object : OnResponseInterface<ResponseDrivers>{
            override fun onSuccess(body: ResponseDrivers?) {
                recycleView?.adapter = DriversResponseAdapter(body?.drivers!!,onClick = { driver:ResponseDriver -> cardViewOnClick(drivers = driver)})
            }

            override fun onError(message: String) {
                alertWarning(message)
            }

            override fun onFailure(t: Throwable?) {
                alertErro(t?.message!!)
            }

        })

    }

    private fun cardViewOnClick(drivers: ResponseDriver){
        toast("Selecionou o motorista ${drivers.name}")
    }
}
