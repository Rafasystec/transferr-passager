package br.com.transferr.passager.webservices

/**
 * Created by Rafael Rocha on 15/02/2018.
 */

import br.com.transferr.model.Car
import br.com.transferr.passager.application.ApplicationTransferr
import br.com.transferr.passager.model.Quadrant
import br.com.transferr.passager.model.responses.ResponseCarsOnline
import br.com.transferr.passager.util.CallRESTMethodsUtil
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
    fun getCarOnline(quadrant:Quadrant):List<ResponseCarsOnline>{
        var stringBuilder = StringBuilder("online/")
        //Far points location
        stringBuilder.append(quadrant.farLeft?.latitude).append("/")
        stringBuilder.append(quadrant.farLeft?.longitude).append("/")
        stringBuilder.append(quadrant.farRight?.latitude).append("/")
        stringBuilder.append(quadrant.farRight?.longitude).append("/")
        //Near points location
        stringBuilder.append(quadrant.nearLeft?.latitude).append("/")
        stringBuilder.append(quadrant.nearLeft?.longitude).append("/")
        stringBuilder.append(quadrant.nearRight?.latitude).append("/")
        stringBuilder.append(quadrant.nearRight?.longitude).append("")
        var url = urlBase+stringBuilder.toString()
        //TODO comment it later

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
        var markers: MutableList<ResponseCarsOnline> = mutableListOf(car1,car2)
        return markers
        //TODO comment for a while
        //var json = callRESTCarOnline.get(url)
        //return callRESTCarOnline.fromJson<MutableList<ResponseCarsOnline>>(json)
    }
}