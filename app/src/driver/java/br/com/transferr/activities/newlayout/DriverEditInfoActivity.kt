package br.com.transferr.activities.newlayout

import android.os.Bundle
import android.view.MenuItem
import br.com.transferr.R
import br.com.transferr.activities.SuperClassActivity
import br.com.transferr.fragments.DriverEditInfo
import br.com.transferr.passenger.extensions.switchFragment
import br.com.transferr.passenger.extensions.switchFragmentToMainContent

class DriverEditInfoActivity : SuperClassActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_driver_edit_info)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        switchFragment(R.id.edtDriverInfoFragment,DriverEditInfo())
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return  when(item.itemId){
            android.R.id.home -> {
                finish()
                true
            }else -> return super.onOptionsItemSelected(item)
        }
    }
}
