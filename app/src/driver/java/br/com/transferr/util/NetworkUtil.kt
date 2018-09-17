package br.com.transferr.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

/**
 * Created by root on 17/02/18.
 */
object NetworkUtil {

    fun isNetworkAvailable(context: Context):Boolean{
        val connectivity = context.getSystemService(Context.CONNECTIVITY_SERVICE)
                as ConnectivityManager
        val networks = connectivity.allNetworks
        return networks
                .map { connectivity.getNetworkInfo(it) }
                .any { it.state == NetworkInfo.State.CONNECTED }
    }
}