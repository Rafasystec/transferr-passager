package br.com.transferr.passager.helpers

import android.graphics.BitmapFactory
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
        if(list != null && !list.isEmpty()){
            list.map {
                        MarkerOptions()
                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_car_black_24dp))
                                .title(it.placa)
                                .snippet(it.model+ " - " + it.cor)
                                .position(LatLng(it.latitude!!, it.longitude!!))
                    }
                    .forEach { markers?.add(it) }

        }
        return markers!!
    }
}