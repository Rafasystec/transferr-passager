package br.com.transferr.activities.newlayout

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
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
        checkCameraPermission()
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
    val PERMISSIONS_FOR_THIS_ACTIVITY = 3
    fun checkCameraPermission(){
        var permissionCheck = ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA)
        if(permissionCheck == PackageManager.PERMISSION_DENIED){
            //Ask for permission
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.CAMERA)){
                //Should explain
            }else{
                ActivityCompat.requestPermissions(this,
                        arrayOf(Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        PERMISSIONS_FOR_THIS_ACTIVITY)
            }
        }
    }
}
