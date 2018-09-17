package br.com.transferr.helpers

import br.com.transferr.model.responses.OnResponseInterface
import br.com.transferr.util.RestUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by idoctor on 22/02/2018.
 */
class HelperCallBackWebService <T>(responseInterface:OnResponseInterface<T>)  : Callback<T> {
    var responseInterface:OnResponseInterface<T>?=null

    init {
        this.responseInterface = responseInterface
        //this.response    = response
    }

    override fun onResponse(call: Call<T>?, response: Response<T>?) {

        if(response != null && response.isSuccessful){
            responseInterface?.onSuccess(response.body())
        }else if(response != null){
            var message = RestUtil<T>().getErroMessage(response)
            responseInterface?.onError(message)
        }
    }

    override fun onFailure(call: Call<T>?, t: Throwable?) {
        responseInterface?.onFailure(t)
    }

}