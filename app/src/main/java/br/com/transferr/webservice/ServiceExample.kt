package br.com.transferr.webservice

import br.com.transferr.helpers.HelperCallBackWebService
import br.com.transferr.model.responses.OnResponseInterface
import br.com.transferr.model.responses.ResponseOK
import br.com.transferr.webservices.SuperWebService
import retrofit2.Call
import retrofit2.http.*

/**
 * Use it only to copy and past as a model
 */
object ServiceExample : SuperWebService(){

    private var service : IServiceExample = ServiceExample.retrofit.create(IServiceExample::class.java)
    fun get( name: String,responseInterface: OnResponseInterface<ResponseOK>){
        service.get(name).enqueue(HelperCallBackWebService(responseInterface))
    }
    fun post(any: Any, responseInterface: OnResponseInterface<Any>){
        service.post(any).enqueue(HelperCallBackWebService(responseInterface))
    }

    fun put(responseInterface: OnResponseInterface<ResponseOK>){
        service.put().enqueue(HelperCallBackWebService(responseInterface))
    }

}

interface IServiceExample {
    @POST("post/url")
    fun post(@Body any: Any): Call<Any>
    @PUT("put/url")
    fun put(): Call<ResponseOK>
    @GET("get/any/object/{name}")
    fun get(@Path("name") name:String): Call<ResponseOK>
}