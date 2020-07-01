package br.com.transferr.passenger.activities

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import br.com.transferr.R
import br.com.transferr.passenger.adapter.TabLocationAdapter
import br.com.transferr.passenger.extensions.switchFragment
import br.com.transferr.passenger.fragments.DriverListFragment
import br.com.transferr.passenger.model.TourOption
import kotlinx.android.synthetic.passenger.activity_location_detail.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import org.jetbrains.anko.yesButton


class LocationDetailActivity : br.com.transferr.passenger.activities.SuperClassActivity() {

    val PERMISSION_TO_PHONE_CALL = 2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_detail)
//--------------------------------------------------------
//        To Active tab layout uncomment below
//--------------------------------------------------------
//        setupViewPageTabs()
//--------------------------------------------------------
        requestPermissionToPhoneCall()
        val tourOption = intent.getSerializableExtra(TourOption.TOUR_PARAMETER_KEY) as TourOption
        val fragment = DriverListFragment()
        if(fragment.arguments == null){
            fragment.arguments = Bundle()
        }
        fragment.arguments!!.putSerializable(TourOption.TOUR_PARAMETER_KEY,tourOption)
        switchFragment(R.id.frmDriversLocation,fragment)
    }

//--------------------------------------------------------
//        To Active tab layout uncomment below
//--------------------------------------------------------
//    fun setupViewPageTabs(){
//        viewPageLocation.offscreenPageLimit = 1
//        viewPageLocation.adapter = br.com.transferr.passenger.adapter.TabLocationAdapter(context, supportFragmentManager, intent.getSerializableExtra(TourOption.TOUR_PARAMETER_KEY) as TourOption)
//        tabLayoutLocation.setupWithViewPager(viewPageLocation)
//        val colorText = ContextCompat.getColor(context,R.color.selectedItemClient)
//        tabLayoutLocation.setTabTextColors(colorText,colorText)
//    }
//--------------------------------------------------------

    private fun requestPermissionToPhoneCall(){
        if(ContextCompat.checkSelfPermission(this@LocationDetailActivity, Manifest.permission.CALL_PHONE) !=
                PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this@LocationDetailActivity, Manifest.permission.CALL_PHONE)){
                alert(R.string.needPermission,R.string.permissionToAccessLocation){
                    yesButton {

                    }
                    noButton {  }
                }.show()
            }else{
                ActivityCompat.requestPermissions(this@LocationDetailActivity,arrayOf(Manifest.permission.CALL_PHONE),PERMISSION_TO_PHONE_CALL)
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
