package br.com.transferr.webservices


import br.com.transferr.model.AnexoPhoto
import br.com.transferr.model.Driver
import br.com.transferr.model.responses.ResponseOK
import retrofit2.Call
import retrofit2.http.*

/**
 * Created by root on 21/02/18.
 */
interface IDriverService  {
    @PUT("driver/profile/photo")
    fun savePhoto(@Body anexoPhoto: AnexoPhoto):Call<ResponseOK>
    @GET("driver/{id}")
    fun getDriver(@Path("id") id:Int): Call<Driver>
    @GET("driver/by/car/{id}")
    fun getDriverByCar(@Path("id") id:Long): Call<Driver>
    @GET("driver/by/user/{userId}")
    fun doGetByUserId(@Path("userId") userId:Long):Call<Driver>
}