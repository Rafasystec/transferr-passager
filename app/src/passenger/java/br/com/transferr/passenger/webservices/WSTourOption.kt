package br.com.transferr.passenger.webservices

import br.com.transferr.passenger.interfaces.OnResponseInterface
import br.com.transferr.passenger.model.Location
import br.com.transferr.passenger.model.TourOption
import br.com.transferr.passenger.model.responses.ResponseLocation
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
    fun doGetById(idTourOption:Long,responseInterface: OnResponseInterface<TourOption>){
        service.doGetById(idTourOption).enqueue(CallBackWS(responseInterface))
    }
    fun getByLocation(idLocation:Long,responseInterface: OnResponseInterface<List<TourOption>>){
        service.doGetByLocation(idLocation).enqueue(CallBackWS(responseInterface))
    }

    interface IWSTourOption{
        @GET(ROOT_URL_TOUR_OPTION)
        fun doGetAll(): Call<List<TourOption>>
        @GET(ROOT_URL_TOUR_OPTION +"/{idTourOption}")
        fun doGetById(@Path("idTourOption")idTourOption:Long): Call<TourOption>
        @GET(ROOT_URL_TOUR_OPTION+"/bylocation/{idLocation}")
        fun doGetByLocation(@Path("idLocation")idLocation:Long): Call<List<TourOption>>
    }
}