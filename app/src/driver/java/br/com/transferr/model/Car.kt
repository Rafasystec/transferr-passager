package br.com.transferr.model

import android.os.Parcel
import br.com.transferr.model.enums.EnumStatus
import br.com.transferr.model.enums.EnumTypeCar

/**
 * Created by idoctor on 08/02/2018.
 */
class Car : Entity(){


    var model: String? = ""
    var carIdentity: String?=null
    var color:String?=null
    var externalEquip:Boolean = false
    var nrSeats:String?=null
    var driver:Driver?= null
    var type: EnumTypeCar?=null
    var status:EnumStatus=EnumStatus.OFFLINE

}


