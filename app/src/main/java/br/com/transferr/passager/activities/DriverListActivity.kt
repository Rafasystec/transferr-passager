package br.com.transferr.passager.activities

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import br.com.transferr.passager.R
import br.com.transferr.passager.adapter.DriversResponseAdapter
import br.com.transferr.passager.extensions.defaultRecycleView
import br.com.transferr.passager.model.responses.ResponseDrivers
import org.jetbrains.anko.toast

class DriverListActivity : SuperClassActivity() {

    private var recycleView: RecyclerView?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_driver_list)
        recycleView = defaultRecycleView(this,R.id.rcDriversFromLocation)

    }

    override fun onResume() {
        super.onResume()
        loadDriversByLocation()
    }

    private fun loadDriversByLocation() {
        var driver = ResponseDrivers()
        with (driver) {
            name = "Assunção de almeida"
            email = "assdealm@gmail.com"
            birthDate = "05/04/1989"
            countryRegister = "UHD-7052"
        }
        var driver1 = ResponseDrivers()
        with (driver1) {
            name = "Avelino Aldeia"
            email = "avelinadd28399@gmail.com"
            birthDate = "05/04/1978"
            countryRegister = "PWS-0221"
        }
        recycleView?.adapter = DriversResponseAdapter(listOf(driver,driver1),onClick = {driver:ResponseDrivers -> cardViewOnClick(drivers = driver)})
    }

    private fun cardViewOnClick(drivers: ResponseDrivers){
        toast("Selecionou ${drivers.name}")
    }
}
