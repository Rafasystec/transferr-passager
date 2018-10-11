package br.com.transferr.webservice

import br.com.transferr.helpers.HelperCallBackWebService
import br.com.transferr.model.TourOption
import br.com.transferr.model.responses.OnResponseInterface
import br.com.transferr.webservices.SuperWebService
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by Rafael Rocha on 04/04/18.
 */

const val BASE_URL_TOUR_OPTION = "touroption"
object TourOptionService: SuperWebService() {
    private var service : ITourOptionService = TourOptionService.retrofit.create(ITourOptionService::class.java)
    fun getAll( responseInterface: OnResponseInterface<List<TourOption>>){
        service.getAll().enqueue(HelperCallBackWebService(responseInterface))
    }
    fun getByDriver(idDriver:Long, responseInterface: OnResponseInterface<List<TourOption>>){
        service.getByDriver(idDriver).enqueue(HelperCallBackWebService(responseInterface))
    }

}

interface ITourOptionService{
    @GET(BASE_URL_TOUR_OPTION)
    fun getAll():Call<List<TourOption>>

    @GET(BASE_URL_TOUR_OPTION+"/bydriver/{idDriver}")
    fun getByDriver(@Path("idDriver") idDriver:Long):Call<List<TourOption>>
}