package br.com.transferr.model

import br.com.transferr.passenger.model.enums.EnumTypeOfDriver
import java.util.*

/**
 * Created by idoctor on 08/02/2018.
 */
class Driver (var name: String,
                   var countryRegister: String,
                   var birthDate: Date): Entity(){


    var phone: Long? = null
    var ddd: Int? = null
    var car: Car? = null
    var whatsapp = 0L
    var typeOfDriver: EnumTypeOfDriver?=null
}