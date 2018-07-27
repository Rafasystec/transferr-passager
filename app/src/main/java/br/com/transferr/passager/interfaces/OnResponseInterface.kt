package br.com.transferr.passager.interfaces

/**
 * Created by Rafael Rocha on 27/07/2018.
 */
interface OnResponseInterface <T>{
    fun onSuccess(body:T?)
    fun onError(message:String)
    fun onFailure( t: Throwable?)
}