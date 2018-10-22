package br.com.transferr.model.responses

import retrofit2.Response

/**
 * Created by idoctor on 22/02/2018.
 */
interface OnResponseInterface<T> {

    fun onSuccess(body:T?)
    fun onError(message:String)
    fun onFailure( t: Throwable?)
}