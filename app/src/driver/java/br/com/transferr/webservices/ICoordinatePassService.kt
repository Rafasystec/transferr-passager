package br.com.transferr.webservices

import br.com.transferr.model.Quadrant
import br.com.transferr.model.responses.ResponsePassengersOnline
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by root on 17/02/18.
 */
interface ICoordinatePassService {

    @GET("pass/coordinates/online/{coord1lon}/{coord1lat}/{coord2lon}/{coord2lat}/{coord3lon}/{coord3lat}/{coord4lon}/{coord4lat}")
    fun getOnlinePassengers(@Path("coord1lon") coord1Lon:Double,
                            @Path("coord1lat") coord1Lat:Double,
                            @Path("coord2lon") coord2Lon:Double,
                            @Path("coord2lat") coord2Lat:Double,
                            @Path("coord3lon") coord3Lon:Double,
                            @Path("coord3lat") coord3Lat:Double,
                            @Path("coord4lon") coord4Lon:Double,
                            @Path("coord4lat") coord4Lat:Double):Call<List<ResponsePassengersOnline>>
}