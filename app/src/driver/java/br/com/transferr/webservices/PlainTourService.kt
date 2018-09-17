package br.com.transferr.webservices

import br.com.transferr.helpers.HelperCallBackWebService
import br.com.transferr.model.Driver
import br.com.transferr.model.PlainTour
import br.com.transferr.model.responses.OnResponseInterface
import br.com.transferr.model.responses.ResponseOK
import br.com.transferr.model.responses.ResponsePlainTour
import retrofit2.Call
import retrofit2.http.*

/**
 * Created by idoctor on 03/04/2018.
 */
const val BASE_URL = "plaintour"
object PlainTourService: SuperWebService() {
    private var service : IPlainTour = retrofit.create(IPlainTour::class.java)
    fun save(plainTour: PlainTour,responseInterface: OnResponseInterface<PlainTour>){
        service.save(plainTour).enqueue(HelperCallBackWebService(responseInterface))
    }

    fun getDriverPlains(id: Long,responseInterface: OnResponseInterface<List<PlainTour>>){
        service.getByDriver(id).enqueue(HelperCallBackWebService(responseInterface))
    }

    fun save(responsePlainTour: ResponsePlainTour,responseInterface: OnResponseInterface<PlainTour>){
        service.save(responsePlainTour).enqueue(HelperCallBackWebService(responseInterface))
    }

    fun increaseSeats(idPlainTour:Long,seats:Int,responseInterface: OnResponseInterface<PlainTour>){
        service.increaseSeats(idPlainTour,seats).enqueue(HelperCallBackWebService(responseInterface))
    }

    fun delete(id: Long,responseInterface: OnResponseInterface<ResponseOK>){
        service.delete(id).enqueue(HelperCallBackWebService(responseInterface))
    }

}

interface IPlainTour{
    @POST(BASE_URL)
    fun save(@Body plainTour: PlainTour): Call<PlainTour>
    @POST(BASE_URL+"/save")
    fun save(@Body responsePlainTour: ResponsePlainTour): Call<PlainTour>
    @GET(BASE_URL+"/{id}")
    fun get(@Path("id") id:Long): Call<PlainTour>
    @GET(BASE_URL+"/bydriver/{id}")
    fun getByDriver(@Path("id") id:Long): Call<List<PlainTour>>
    @PUT(BASE_URL+"/increase/seats/{idPlainTour}/{seats}")
    fun increaseSeats(@Path("idPlainTour") idPlainTour:Long,@Path("seats") seats:Int): Call<PlainTour>
    @DELETE(BASE_URL+"/{id}")
    fun delete(@Path("id") id:Long): Call<ResponseOK>
}