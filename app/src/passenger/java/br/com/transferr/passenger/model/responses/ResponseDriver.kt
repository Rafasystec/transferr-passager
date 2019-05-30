package br.com.transferr.passenger.model.responses

import br.com.transferr.passenger.model.enums.EnumTypeOfDriver

/**
 * Created by Rafael Rocha on 31/07/2018.
 */
class ResponseDriver {

    var id = 0.toLong()
    var name = ""
    var birthDate = ""
    //Car plate
    var countryRegister = ""
    var email=""
    var imgProfileUrl:String?=null
    var phone = ""
    var whatsapp=""
    var nameOfCar=""
   var type: EnumTypeOfDriver? = null

}