package br.com.transferr.passager.webservices

import br.com.transferr.passager.interfaces.OnResponseInterface
import br.com.transferr.passager.model.Location
import br.com.transferr.passager.model.TourOption
import br.com.transferr.passager.model.responses.ResponseLocation
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by Rafael Rocha on 07/08/2018.
 */
const val ROOT_URL_TOUR_OPTION = "touroption"

object WSTourOption : BaseWebService(){
    private var service : WSTourOption.IWSTourOption = WSTourOption.retrofit.create(WSTourOption.IWSTourOption::class.java)
    fun doGetAll(responseInterface: OnResponseInterface<List<TourOption>>){
        service.doGetAll().enqueue(CallBackWS(responseInterface))
    }
    fun doGetById(idLocation:Long,responseInterface: OnResponseInterface<TourOption>){
        service.doGetById(idLocation).enqueue(CallBackWS(responseInterface))
    }

    interface IWSTourOption{
        @GET(ROOT_URL_TOUR_OPTION)
        fun doGetAll(): Call<List<TourOption>>
        @GET(ROOT_URL_TOUR_OPTION +"/{idTourOption}")
        fun doGetById(@Path("idTourOption")idTourOption:Long): Call<TourOption>
    }
}