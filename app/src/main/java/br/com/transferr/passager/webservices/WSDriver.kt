package br.com.transferr.passager.webservices

import br.com.transferr.passager.interfaces.OnResponseInterface
import br.com.transferr.passager.model.responses.ResponseDrivers
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by Rafael Rocha on 31/07/2018.
 */
const val ROOT_URL_DRIVER = "driver"
object WSDriver : BaseWebService() {

    private var service : IWSDriver = retrofit.create(IWSDriver::class.java)

    fun doGetByLocation(idLocation: Long, responseInterface: OnResponseInterface<ResponseDrivers>){
        service.doGetByLocation(idLocation).enqueue(CallBackWS(responseInterface))
    }

    interface IWSDriver{
        @GET(ROOT_URL_DRIVER+"/by/location/{idLocation}")
        fun doGetByLocation(@Path("idLocation") idLocation:Long):Call<ResponseDrivers>
    }
}