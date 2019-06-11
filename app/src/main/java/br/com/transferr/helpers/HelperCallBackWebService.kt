package br.com.transferr.helpers

import android.app.ProgressDialog
import android.support.v4.app.FragmentActivity
import br.com.transferr.R
import br.com.transferr.application.ApplicationTransferr
import br.com.transferr.model.responses.OnResponseInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.HttpURLConnection

/**
 * Created by idoctor on 22/02/2018.
 */
class HelperCallBackWebService <T>(responseInterface:OnResponseInterface<T>, activity: FragmentActivity?=null, progress: ProgressDialog? =null)  : Callback<T> {
    var responseInterface:OnResponseInterface<T>?=null
    private var localActivity:FragmentActivity?= null
    private var localProgress:ProgressDialog?=null

    init {
        this.responseInterface = responseInterface
        this.localActivity = activity
        this.localProgress = progress
    }

    override fun onResponse(call: Call<T>?, response: Response<T>?) {

        if(response != null && response.isSuccessful){
            responseInterface?.onSuccess(response.body())
        }else if(response != null){
            val code = response.code()
            val resId = if(code == HttpURLConnection.HTTP_BAD_REQUEST ||
                    code == HttpURLConnection.HTTP_INTERNAL_ERROR) {
                R.string.errorSystem
            }else if(code == HttpURLConnection.HTTP_UNAVAILABLE){
                R.string.errorExecutTheFunction
            }else{
                R.string.errorExecutTheFunction
            }
            responseInterface?.onError(ApplicationTransferr.getString(resId!!),localActivity)
        }
    }

    override fun onFailure(call: Call<T>?, t: Throwable?) {
        responseInterface?.onFailure(t,localActivity,this.localProgress)
    }

}