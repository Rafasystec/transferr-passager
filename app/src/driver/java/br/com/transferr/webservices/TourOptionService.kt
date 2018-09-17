package br.com.transferr.webservices

import br.com.transferr.helpers.HelperCallBackWebService
import br.com.transferr.model.TourOption
import br.com.transferr.model.responses.OnResponseInterface
import retrofit2.Call
import retrofit2.http.GET

/**
 * Created by root on 04/04/18.
 */

const val BASE_URL_TOUR_OPTION = "touroption"
object TourOptionService:SuperWebService() {
    private var service : ITourOptionService = TourOptionService.retrofit.create(ITourOptionService::class.java)
    fun getAll( responseInterface: OnResponseInterface<List<TourOption>>){
        service.getAll().enqueue(HelperCallBackWebService(responseInterface))
    }

}

interface ITourOptionService{
    @GET(BASE_URL_TOUR_OPTION)
    fun getAll():Call<List<TourOption>>
}