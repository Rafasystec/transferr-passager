package br.com.transferr.passager.helpers

import android.graphics.BitmapFactory
import br.com.transferr.model.Car
import br.com.transferr.passager.R
import br.com.transferr.passager.model.responses.ResponseCarsOnline
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

/**
 * Created by idoctor on 15/02/2018.
 */
object HelperCar {

    fun transformInMarkers(list:List<ResponseCarsOnline>):MutableList<MarkerOptions>{
        var markers:MutableList<MarkerOptions>?= mutableListOf()
        if(!list.isEmpty()){
            list.map {
                        //MarkerOptions()
                        //        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_car_black_24dp))
                        //        .title(it.placa)
                        //        .snippet(it.model+ " - " + it.cor)
                        //        .position(LatLng(it.latitude!!, it.longitude!!))
                        addMarkCarOnTheMap(it)
                    }
                    .forEach { markers?.add(it!!) }

        }
        return markers!!
    }

    fun addMarkCarOnTheMap(car:ResponseCarsOnline): MarkerOptions? {
        if(car.latitude == null || car.longitude == null){
            car.latitude = Double.MIN_VALUE
            car.longitude = Double.MIN_VALUE
        }
        var marck = MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_car_black_24dp))
                .title(car.placa)
                .snippet(car.model+ " - " + car.cor)
                .position(LatLng(car.latitude!!, car.longitude!!))
        return marck
    }
}