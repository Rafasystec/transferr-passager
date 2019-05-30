package br.com.transferr.passenger.model.responses

import br.com.transferr.model.enums.Color
import br.com.transferr.model.enums.EnumLanguage
import br.com.transferr.model.enums.EnumTypeCar
import java.io.Serializable
import java.util.*

/**
 * Created by Rafael Rocha on 15/02/2018.
 */
class ResponseCarsOnline : Serializable{
    var driverId = 0.toLong()
    var id = 0.toLong()
    var photo: String? = null
    var model: String? = null
    var placa: String? = null
    var cor: Color? = null
    var name: String? = null
    var latitude: Double? = null
    var longitude: Double? = null
    var phone: Long = 0L
    var whatsapp: Long = 0L
    var ddd: Int = 0
    var type_car: EnumTypeCar = EnumTypeCar.BUGGY
    var date: Map<EnumLanguage,String>?=null

    companion object {
        val PARAM_CAR_OBJECT = "carParameter"
        val PARAM_MARKER_OBJ = "MarkerParameter"
    }
}