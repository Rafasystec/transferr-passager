package br.com.transferr.passenger.webservices

import br.com.transferr.passenger.interfaces.OnResponseInterface
import br.com.transferr.passenger.model.SubCountry
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by Rafael Rocha on 27/07/2018.
 */
const val ROOT_URL_SUBCOUNTRY = "subcountry"
object WSSubCountry : BaseWebService(){

    private var service : IWSSubCountry = WSSubCountry.retrofit.create(IWSSubCountry::class.java)
    fun doGetAllByCountry(idCountry: Long,responseInterface: OnResponseInterface<List<SubCountry>>){
        service.doGetAllByCountry(idCountry).enqueue(CallBackWS(responseInterface))
    }

    interface IWSSubCountry{
        @GET(ROOT_URL_SUBCOUNTRY+"/by/country/{idCountry}")
        fun doGetAllByCountry(@Path("idCountry") idCountry:Long): Call<List<SubCountry>>
    }
}