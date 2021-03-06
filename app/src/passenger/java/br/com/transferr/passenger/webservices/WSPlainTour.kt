package br.com.transferr.passenger.webservices

import android.app.ProgressDialog
import android.support.v4.app.FragmentActivity
import br.com.transferr.helpers.HelperCallBackWebService
import br.com.transferr.model.responses.OnResponseInterface
import br.com.transferr.passenger.model.responses.ResponsePlainsByTourAndLocation
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by Rafael Rocha on 10/08/2018.
 */
const val ROOT_URL_PLAIN_TOUR = "plaintour"
object WSPlainTour : BaseWebService(){
    private val service : WSPlainTour.IWSPlainTour = WSPlainTour.retrofit.create(WSPlainTour.IWSPlainTour::class.java)
    /*
    fun getByTourOption(idTourOption: Long,responseInterface: OnResponseInterface<List<PlainTour>>){
        service.doGetByTourOption(idTourOption).enqueue(HelperCallBackWebService(responseInterface))
    }
    */
    fun getByTourAndLocation(idTourOption: Long, responseInterface: OnResponseInterface<ResponsePlainsByTourAndLocation>, activity: FragmentActivity?, progress: ProgressDialog){
        service.doGetByTourAndLocation(idTourOption).enqueue(HelperCallBackWebService(responseInterface,activity,progress))
    }
    interface IWSPlainTour{
        //@GET(ROOT_URL_PLAIN_TOUR)
        //fun doGetByTourOption(@Path("idTourOption") idTourOption:Long):Call<List<PlainTour>>
        @GET(ROOT_URL_PLAIN_TOUR+"/by/tour/and/location/{idTourOption}")
        fun doGetByTourAndLocation(@Path("idTourOption") idTourOption:Long):Call<ResponsePlainsByTourAndLocation>
    }
}