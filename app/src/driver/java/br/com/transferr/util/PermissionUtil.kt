package br.com.transferr.util

import android.app.Activity
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import br.com.transferr.activities.InitialActivity

/**
 * Created by idoctor on 09/02/2018.
 */

object PermissionUtil{
    fun validate(activity: Activity, code: Int,vararg permissions: String):Boolean{
        val list = ArrayList<String>()
        for (permission in permissions){
            val allowed = ContextCompat.checkSelfPermission(activity,permission) == PackageManager.PERMISSION_GRANTED
            if(!allowed){
                list.add(permission)
            }
        }
        if(list.isEmpty()){
            return true
        }
        val newPermissions = arrayOfNulls<String>(list.size)
        ActivityCompat.requestPermissions(activity,newPermissions,1)
        return false
    }
}