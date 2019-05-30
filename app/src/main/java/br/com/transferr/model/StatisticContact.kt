package br.com.transferr.model

import java.io.Serializable
import br.com.transferr.model.enums.EnumDeviceType
import br.com.transferr.model.enums.EnumTypeContact
import java.util.*


/**
 * Created by idoctor on 30/05/2019.
 */
class StatisticContact : Serializable{
    var idDriver: Long = 0
    var type: EnumTypeContact? = null
    var date: Date? = null
    var longitude: Double = 0.toDouble()
    var latitude: Double = 0.toDouble()
    var city: String? = null
    var state: String? = null
    var country: String? = null
    var device: EnumDeviceType? = null

}