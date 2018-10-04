package br.com.transferr.activities.newlayout

import android.os.Bundle
import br.com.transferr.R
import br.com.transferr.activities.SuperClassActivity
import br.com.transferr.fragments.DriverAddPlanTourFragment
import br.com.transferr.passenger.extensions.switchFragment

class DriverAddPlainTourActivity : SuperClassActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_driver_add_plain_tour)
        switchFragment(R.id.addPlanTourFragment,DriverAddPlanTourFragment())
    }
}
