package br.com.transferr.model

import java.util.*

/**
 * Created by idoctor on 08/02/2018.
 */
class Driver (var name: String,
                   var countryRegister: String,
                   var birthDate: Int): Entity(){


    var phone: Long? = null
    var ddd: Int? = null
    var car: Car? = null
}