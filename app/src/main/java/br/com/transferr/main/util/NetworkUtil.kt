package br.com.transferr.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import br.com.transferr.main.util.ExecutorServiceUtil
import br.com.transferr.main.util.InternetUtil
import java.util.concurrent.Callable
import java.util.concurrent.TimeUnit

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

    fun hasConnection(context: Context):Boolean{
        return ExecutorServiceUtil.timedCall<Boolean>(Callable {
            InternetUtil.isNetworkConnected(context)
        },2, TimeUnit.SECONDS)
    }

    fun hasInternetConnection():Boolean{
        return ExecutorServiceUtil.timedCall(Callable {
            InternetUtil.isInternetAvaliable()
        },2,TimeUnit.SECONDS)
    }

}