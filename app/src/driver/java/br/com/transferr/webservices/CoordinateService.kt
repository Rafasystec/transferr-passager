package br.com.transferr.webservices

import br.com.transferr.helpers.HelperCallBackWebService
import br.com.transferr.model.Coordinates
import br.com.transferr.model.responses.OnResponseInterface
import br.com.transferr.model.responses.RequestCoordinatesUpdate
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT

/**
 * Created by Rafael Rocha on 08/02/2018.
 */

object CoordinateService :SuperWebService(){

    private var service: ICoordinateService = CoordinateService.retrofit.create(ICoordinateService::class.java)

    fun save(coordinates: Coordinates,responseInterface: OnResponseInterface<Coordinates>){
        service.save(coordinates).enqueue(HelperCallBackWebService(responseInterface))
    }

    fun updateMyLocation(coordinatesUpdate: RequestCoordinatesUpdate,responseInterface: OnResponseInterface<RequestCoordinatesUpdate>){
        service.update(coordinatesUpdate).enqueue(HelperCallBackWebService(responseInterface))
    }

    interface ICoordinateService{
        @POST("coordinatescar")
        fun save(@Body coordinates: Coordinates): Call<Coordinates>
        @PUT("coordinatescar/actual/location")
        fun update(@Body coordinatesUpdate: RequestCoordinatesUpdate): Call<RequestCoordinatesUpdate>
    }


}