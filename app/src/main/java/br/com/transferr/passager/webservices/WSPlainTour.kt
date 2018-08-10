package br.com.transferr.passager.webservices

import br.com.transferr.passager.interfaces.OnResponseInterface
import br.com.transferr.passager.model.PlainTour
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by Rafael Rocha on 10/08/2018.
 */
const val ROOT_URL_PLAIN_TOUR = "plaintour"
object WSPlainTour : BaseWebService(){
    private val service : WSPlainTour.IWSPlainTour = WSPlainTour.retrofit.create(WSPlainTour.IWSPlainTour::class.java)
    fun getByTourOption(idTourOption: Long,responseInterface: OnResponseInterface<List<PlainTour>>){
        service.doGetByTourOption(idTourOption).enqueue(CallBackWS(responseInterface))
    }
    interface IWSPlainTour{
        @GET(ROOT_URL_PLAIN_TOUR)
        fun doGetByTourOption(@Path("idTourOption") idTourOption:Long):Call<List<PlainTour>>
    }
}