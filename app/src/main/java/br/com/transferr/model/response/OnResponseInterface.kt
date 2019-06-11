package br.com.transferr.model.responses

import android.app.ProgressDialog
import android.support.v4.app.FragmentActivity
import android.util.Log
import br.com.transferr.R
import br.com.transferr.passenger.extensions.showAlertError
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Created by Rafael Rocha on 22/02/2018.
 */
interface OnResponseInterface<T> {

    fun onSuccess(body:T?)
    fun onError(message:String,activity: FragmentActivity?=null,progress:ProgressDialog?=null){
        progress?.dismiss()
        activity?.showAlertError(message)
    }
    /**
     * Overrid it only if you dont have a context
     */
    fun onFailure(t: Throwable?, activity: FragmentActivity?, progress: ProgressDialog?){
        progress?.dismiss()
        when (t) {
            is UnknownHostException -> activity?.showAlertError(activity.getString(R.string.connectionFailed))
            is SocketTimeoutException -> activity?.showAlertError(activity.getString(R.string.errorServerNotResponding))
            else -> activity?.showAlertError(activity.getString(R.string.connectionFailed))
        }
    }

    fun onError(message:String){
        Log.i("ERROR",message)
    }

    fun onFailure( t: Throwable?){
        Log.e("FAIL",t?.message)
    }
}