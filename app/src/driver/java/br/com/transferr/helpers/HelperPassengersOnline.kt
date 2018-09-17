package br.com.transferr.helpers

import br.com.transferr.R
import br.com.transferr.model.responses.ResponsePassengersOnline
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

/**
 * Created by root on 16/02/18.
 */
object HelperPassengersOnline {
    fun transformInMarkers(list:List<ResponsePassengersOnline>):MutableList<MarkerOptions>{
        var markers:MutableList<MarkerOptions>?= mutableListOf()
        if(list != null && !list.isEmpty()){
            list.map {
                MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_face_black_24dp))
                        .title("Passageiro")
                        .snippet("11,5 km")
                        .position(LatLng(it.latitude!!, it.longitude!!))
            }
                    .forEach { markers?.add(it) }

        }
        return markers!!
    }
}