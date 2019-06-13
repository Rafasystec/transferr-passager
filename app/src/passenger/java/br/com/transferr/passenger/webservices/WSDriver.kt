package br.com.transferr.passenger.webservices

import android.app.ProgressDialog
import android.support.v4.app.FragmentActivity
import br.com.transferr.helpers.HelperCallBackWebService
import br.com.transferr.model.Driver
import br.com.transferr.model.responses.OnResponseInterface
import br.com.transferr.passenger.model.responses.ResponseDrivers
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by Rafael Rocha on 31/07/2018.
 */
const val ROOT_URL_DRIVER = "driver"
object WSDriver : BaseWebService() {

    private var service : IWSDriver = retrofit.create(IWSDriver::class.java)

    fun doGetByLocation(idLocation: Long, responseInterface: OnResponseInterface<ResponseDrivers>, activity: FragmentActivity?, progress: ProgressDialog){
        service.doGetByLocation(idLocation).enqueue(HelperCallBackWebService(responseInterface,activity,progress))
    }

    fun doGetByUserId(userId: Long,responseInterface: OnResponseInterface<Driver>){
        service.doGetByUserId(userId).enqueue(HelperCallBackWebService(responseInterface))
    }

    interface IWSDriver{
        @GET(ROOT_URL_DRIVER+"/by/location/{idLocation}")
        fun doGetByLocation(@Path("idLocation") idLocation:Long):Call<ResponseDrivers>
        @GET(ROOT_URL_DRIVER+"/by/user/{userId}")
        fun doGetByUserId(@Path("userId") userId:Long):Call<Driver>
    }
}