package br.com.transferr.webservices

import br.com.transferr.model.Quadrant
import br.com.transferr.model.responses.ResponsePassengersOnline
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by Rafael Rocha on 16/02/18.
 */
object CoordinatePassService : SuperWebService(){
    private var service: ICoordinatePassService
    init {
        val retrofit = Retrofit.Builder()
                .baseUrl(CoordinatePassService.urlBase)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        CoordinatePassService.service = retrofit.create(ICoordinatePassService::class.java)
    }
    fun getOnlinePassengers(quadrant: Quadrant):List<ResponsePassengersOnline>{
        val call = service.getOnlinePassengers(
                quadrant.farLeftLng,
                quadrant.farLeftLat,
                quadrant.farRightLng,
                quadrant.farRightLat,
                quadrant.nearLeftLng,
                quadrant.nearLeftLat,
                quadrant.nearRightLng,
                quadrant.nearRightLat)
        return call.execute().body()!!
    }
}