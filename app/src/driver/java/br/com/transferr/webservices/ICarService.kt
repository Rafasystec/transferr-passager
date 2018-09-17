package br.com.transferr.webservices

import br.com.transferr.model.Car
import br.com.transferr.model.responses.RequestCoordinatesUpdate
import br.com.transferr.model.responses.ResponseOK
import retrofit2.Call
import retrofit2.http.*

/**
 * Created by root on 17/02/18.
 */
interface ICarService {
    @GET("car/{id}")
    fun getCar(@Path("id") id:Long):Call<Car>
    @GET("car/by/user/{idUser}")
    fun getCarByUser(@Path("idUser") id:Long):Call<Car>
    @PUT("car/online")
    fun online(@Body resquestUpdate: RequestCoordinatesUpdate):Call<ResponseOK>
    @PUT("car/offline")
    fun offline(@Body resquestUpdate: RequestCoordinatesUpdate):Call<ResponseOK>
}