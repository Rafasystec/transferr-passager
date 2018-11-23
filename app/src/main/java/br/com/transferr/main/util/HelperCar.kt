package br.com.transferr.passenger.helpers

import br.com.transferr.model.enums.EnumTypeCar
import br.com.transferr.passenger.extensions.toJson
import br.com.transferr.passenger.model.responses.ResponseCarsOnline
import br.com.transferr.util.ImageUtil
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


/**
 * Created by Rafael Rocha on 15/02/2018.
 */
object HelperCar {

    fun transformInMarkers(list:List<ResponseCarsOnline>):MutableList<MarkerOptions>{
        var markers:MutableList<MarkerOptions>?= mutableListOf()
        if(!list.isEmpty()){
            list.map {
                addMarkCarOnTheMap(it)
            }.forEach { markers?.add(it!!) }
        }
        return markers!!
    }

    fun addMarkCarOnTheMap(car:ResponseCarsOnline): MarkerOptions? {
        if(car.latitude == null || car.longitude == null){
            car.latitude = Double.MIN_VALUE
            car.longitude = Double.MIN_VALUE
        }
        var icon = if(car.type_car == EnumTypeCar.BUGGY){
            //BitmapDescriptorFactory.fromResource(R.mipmap.buggtamarelo)
            BitmapDescriptorFactory.fromBitmap(ImageUtil.resizeMapIcons("buggtamarelo",100,100))
        }else if(car.type_car == EnumTypeCar.BUS){
            //BitmapDescriptorFactory.fromResource(R.mipmap.onibus)
            BitmapDescriptorFactory.fromBitmap(ImageUtil.resizeMapIcons("onibus",100,100))
        }else{
            //BitmapDescriptorFactory.fromResource(R.mipmap.toyota_hilux)
            BitmapDescriptorFactory.fromBitmap(ImageUtil.resizeMapIcons("toyota_hilux",90,70))
        }

        var marck = MarkerOptions()
                //.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_car_black_24dp))
                .icon(icon)
                .title(car.name)
                .snippet(car.toJson())
                .position(LatLng(car.latitude!!, car.longitude!!))
        return marck
    }
}