package br.com.transferr.passenger.webservices

import br.com.transferr.passenger.interfaces.OnResponseInterface
import br.com.transferr.passenger.model.Location
import br.com.transferr.passenger.model.responses.ResponseLocation
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by Rafael Rocha on 27/07/2018.
 */
const val ROOT_URL_LOCATION = "location"
object WSLocation : BaseWebService(){
    private var service : WSLocation.IWSLocation = WSLocation.retrofit.create(WSLocation.IWSLocation::class.java)
    fun doGetBySubCountry(idSubcountry: Long,responseInterface: OnResponseInterface<List<ResponseLocation>>){
        service.doGetBySubCountry(idSubcountry).enqueue(CallBackWS(responseInterface))
    }
    fun doGetById(idLocation:Long,responseInterface: OnResponseInterface<Location>){
        service.doGetById(idLocation).enqueue(CallBackWS(responseInterface))
    }

    fun getAll(responseInterface: OnResponseInterface<List<Location>>){
        service.doGetAll().enqueue(CallBackWS(responseInterface))
    }

    interface IWSLocation{
        @GET(ROOT_URL_LOCATION+"/by/subcountry/{idSubcountry}")
        fun doGetBySubCountry(@Path("idSubcountry") idSubcountry:Long):Call<List<ResponseLocation>>
        @GET(ROOT_URL_LOCATION+"/{idLocation}")
        fun doGetById(@Path("idLocation")idLocation:Long):Call<Location>
        @GET(ROOT_URL_LOCATION)
        fun doGetAll():Call<List<Location>>
    }
}