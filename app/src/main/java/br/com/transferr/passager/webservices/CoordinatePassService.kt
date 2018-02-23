package br.com.transferr.passager.webservices

import android.content.Context
import android.location.Location
import br.com.transferr.model.Car
import br.com.transferr.passager.application.ApplicationTransferr
import br.com.transferr.passager.extensions.toJson
import br.com.transferr.passager.model.CoordinatesPass
import br.com.transferr.passager.util.CallRESTMethodsUtil
import br.com.transferr.util.Prefes

/**
 * Created by root on 16/02/18.
 */
class CoordinatePassService(var context:Context) {

    private var urlBase = ApplicationTransferr.getInstance().URL_BASE
    var callREST = CallRESTMethodsUtil<CoordinatesPass>()

    fun update(location:Location){
        var prefer = Prefes(context)
        var coordinates =  CoordinatesPass()
        coordinates.latitude = location.latitude
        coordinates.longitude= location.longitude
        //TODO take the real value in on create method
        coordinates.id = 1
        coordinates.token = prefer.prefsToken
        callREST.put(urlBase+"pass/coordinates",coordinates.toJson())
    }
}