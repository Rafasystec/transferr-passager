package br.com.transferr.passenger.webservices

/**
 * Created by Rafael Rocha on 15/02/2018.
 */

import br.com.transferr.application.ApplicationTransferr
import br.com.transferr.model.Car
import br.com.transferr.model.Quadrant
import br.com.transferr.passenger.model.responses.ResponseCarsOnline
import br.com.transferr.passenger.util.CallRESTMethodsUtil


/**
 * Created by Rafael Rocha 12/02/18.
 */
class CarServiceMain {
    private var urlBase = ApplicationTransferr.getInstance().URL_BASE
    var callREST = CallRESTMethodsUtil<Car>()
    fun getCar(id:Int): Car {
        var json = callREST.get(urlBase+"car/$id")
        var car = callREST.fromJson<Car>(json)
        Thread.sleep(2*1000)
        return car
    }
    var callRESTCarOnline = CallRESTMethodsUtil<MutableList<ResponseCarsOnline>>()
    fun getCarOnline(quadrant: Quadrant):List<ResponseCarsOnline>{
        var stringBuilder = StringBuilder("car/online/")
        //Far points location
        stringBuilder.append(quadrant.farLeftLng).append("/")
        stringBuilder.append(quadrant.farLeftLat).append("/")
        stringBuilder.append(quadrant.farRightLng).append("/")
        stringBuilder.append(quadrant.farRightLat).append("/")
        //Near points location
        stringBuilder.append(quadrant.nearLeftLng).append("/")
        stringBuilder.append(quadrant.nearLeftLat).append("/")
        stringBuilder.append(quadrant.nearLeftLng).append("/")
        stringBuilder.append(quadrant.nearRightLat).append("")
        var url = urlBase+stringBuilder.toString()
        var json = callRESTCarOnline.get(url)
        return callRESTCarOnline.fromJson<MutableList<ResponseCarsOnline>>(json)
    }
}