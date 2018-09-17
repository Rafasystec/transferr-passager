package br.com.transferr.extensions

import android.util.Log
import com.google.firebase.messaging.RemoteMessage

/**
 * Created by idoctor on 09/02/2018.
 */

val DEBUG = "DEBUG"

fun Any.log(message: String){
    Log.d(DEBUG,message)
}