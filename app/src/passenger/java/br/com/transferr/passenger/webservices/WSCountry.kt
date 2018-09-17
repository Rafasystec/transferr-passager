package br.com.transferr.passenger.webservices

import br.com.transferr.passenger.interfaces.OnResponseInterface
import br.com.transferr.passenger.model.Country
import retrofit2.Call
import retrofit2.http.GET

/**
 * Created by Rafael Rocha on 27/07/2018.
 */
const val ROOT_URL_COUNTRY = "country"
object WSCountry : BaseWebService(){

    private var service : IWSCountry = retrofit.create(IWSCountry::class.java)
    fun doGetAll(responseInterface: OnResponseInterface<List<Country>>){
        service.doGetAll().enqueue(CallBackWS(responseInterface))
    }

    interface IWSCountry{
        @GET(ROOT_URL_COUNTRY)
        fun doGetAll():Call<List<Country>>
    }
}