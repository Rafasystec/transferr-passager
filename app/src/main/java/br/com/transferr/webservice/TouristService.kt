package br.com.transferr.webservice

import br.com.transferr.helpers.HelperCallBackWebService
import br.com.transferr.model.Tourist
import br.com.transferr.model.responses.OnResponseInterface
import br.com.transferr.model.responses.ResponseOK
import br.com.transferr.webservices.SuperWebService
import retrofit2.Call
import retrofit2.http.*

/**
 * Use it only to copy and past as a model
 */
object TouristService : SuperWebService(){

    private var service : ITouristService = TouristService.retrofit.create(ITouristService::class.java)
//    fun get( name: String,responseInterface: OnResponseInterface<ResponseOK>){
//        service.get(name).enqueue(HelperCallBackWebService(responseInterface))
//    }
    fun post(tourist: Tourist, responseInterface: OnResponseInterface<Tourist>){
        service.post(tourist).enqueue(HelperCallBackWebService(responseInterface))
    }

//    fun put(responseInterface: OnResponseInterface<ResponseOK>){
//        service.put().enqueue(HelperCallBackWebService(responseInterface))
//    }

}

interface ITouristService {
    @POST("tourist")
    fun post(@Body tourist:Tourist): Call<Tourist>
//    @PUT("put/url")
//    fun put(): Call<ResponseOK>
//    @GET("get/any/object/{name}")
//    fun get(@Path("name") name:String): Call<ResponseOK>
}