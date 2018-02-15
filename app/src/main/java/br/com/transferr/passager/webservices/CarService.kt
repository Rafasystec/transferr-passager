package br.com.transferr.passager.webservices

/**
 * Created by Rafael Rocha on 15/02/2018.
 */

import br.com.transferr.model.Car
import br.com.transferr.passager.application.ApplicationTransferr
import br.com.transferr.passager.model.Quadrant
import br.com.transferr.passager.model.responses.ResponseCarsOnline
import br.com.transferr.passager.util.CallRESTMethodsUtil
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


/**
 * Created by Rafael Rocha 12/02/18.
 */
class CarService {
    private var urlBase = ApplicationTransferr.getInstance().URL_BASE
    var callREST = CallRESTMethodsUtil<Car>()
    fun getCar(id:Int): Car {
        var json = callREST.get(urlBase+"car/$id")
        var car = callREST.fromJson<Car>(json)
        Thread.sleep(2*1000)
        return car
    }
    var callRESTCarOnline = CallRESTMethodsUtil<MutableList<ResponseCarsOnline>>()
    fun getCarOnline(coordinatesStr:String):List<ResponseCarsOnline>{
        var stringBuilder = StringBuilder("car/online/")
        stringBuilder.append(coordinatesStr)
        var url = urlBase+stringBuilder.toString()
        //TODO comment it later
/*
        var car1 = ResponseCarsOnline()
        car1.latitude = -3.73674356
        car1.longitude = -38.49615601
        car1.cor = "AZUL"
        car1.model = "HILUX"
        car1.placa = "HHW-9870"

        var car2 = ResponseCarsOnline()
        car2.latitude = -3.73957428
        car2.longitude = -38.4938627
        car2.cor = "PRETO"
        car2.model = "S-10"
        car2.placa = "OHQ-5672"

        var car3 = ResponseCarsOnline()
        car3.latitude  = -3.73682921
        car3.longitude = -38.48748711
        car3.cor = "BEGE DESERT"
        car3.model = "D-20"
        car3.placa = "OHJ-0072"

        var markers: MutableList<ResponseCarsOnline> = mutableListOf(car1,car2,car3)
        return markers
        */
        //TODO comment for a while
        var json = callRESTCarOnline.get(url)
        return callRESTCarOnline.fromJson<MutableList<ResponseCarsOnline>>(json)
    }
}