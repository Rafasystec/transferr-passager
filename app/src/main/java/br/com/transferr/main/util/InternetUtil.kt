package br.com.transferr.main.util

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.support.design.widget.Snackbar
import android.view.View
import br.com.transferr.R

/**
 * Created by idoctor on 08/02/2018.
 */
object InternetUtil {

    @SuppressLint("NewApi")
    fun isNetworkAvailable(context:Context):Boolean{
        val connectivity = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networks = connectivity.allNetworks
        return networks
                .map { connectivity.getNetworkInfo(it) }
                .any { it.state == NetworkInfo.State.CONNECTED }
    }

    /*
    fun showSnackbarNoConnection(view:View, context: Context): Boolean{
        return if(!isNetworkAvailable(context)) {
            Snackbar.make(view, R.string.noInternetConnection, Snackbar.LENGTH_LONG).show()
            false
        }else true
    }
    */
}