package br.com.transferr.util

import android.annotation.SuppressLint
import android.content.Context
import android.telephony.TelephonyManager

/**
 * Created by idoctor on 16/02/2018.
 */
class DeviceId {

    @SuppressLint("MissingPermission")
    public fun getDeviceID(context:Context) : String{
        var telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return telephonyManager.subscriberId
    }

}