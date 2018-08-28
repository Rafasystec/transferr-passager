package br.com.transferr.passager.activities

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import br.com.transferr.passager.R
import br.com.transferr.passager.adapter.TabLocationAdapter
import br.com.transferr.passager.model.TourOption
import kotlinx.android.synthetic.main.activity_location_detail.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import org.jetbrains.anko.yesButton


class LocationDetailActivity : SuperClassActivity() {

    val PERMISSION_TO_PHONE_CALL = 2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_detail)
        setupViewPageTabs()
        requestPermissionToPhoneCall()
    }

    fun setupViewPageTabs(){
        viewPageLocation.offscreenPageLimit = 1
        viewPageLocation.adapter = TabLocationAdapter(context,supportFragmentManager,intent.getSerializableExtra(TourOption.TOUR_PARAMETER_KEY) as TourOption)
        tabLayoutLocation.setupWithViewPager(viewPageLocation)
        val colorText = ContextCompat.getColor(context,R.color.colorPrimaryDark)
        tabLayoutLocation.setTabTextColors(colorText,colorText)
    }

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
