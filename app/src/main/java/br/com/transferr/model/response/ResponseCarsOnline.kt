package br.com.transferr.passenger.model.responses

import android.provider.ContactsContract
import br.com.transferr.model.enums.EnumTypeCar
import java.io.Serializable

/**
 * Created by idoctor on 15/02/2018.
 */
class ResponseCarsOnline : Serializable{
    var photo: String? = null
    var model: String? = null
    var placa: String? = null
    var cor: String? = null
    var name: String? = null
    var latitude: Double? = null
    var longitude: Double? = null
    var phone: Long = 0L
    var whatsapp: Long = 0L
    var ddd: Int = 0
    var type_car: EnumTypeCar = EnumTypeCar.BUGGY

    companion object {
        val PARAM_CAR_OBJECT = "carParameter"
        val PARAM_MARKER_OBJ = "MarkerParameter"
    }
}