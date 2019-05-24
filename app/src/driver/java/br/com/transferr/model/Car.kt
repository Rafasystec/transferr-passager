package br.com.transferr.model

import android.os.Parcel
import br.com.transferr.model.enums.Color
import br.com.transferr.model.enums.EnumStatus
import br.com.transferr.model.enums.EnumTypeCar

/**
 * Created by Rafael Rocha on 08/02/2018.
 */
class Car : Entity(){


    var model: String? = ""
    var carIdentity: String?=null
    var color:Color?=null
    var externalEquip:Boolean = false
    var nrSeats:String?=null
    var driver:Driver?= null
    var type: EnumTypeCar?=null
    var status:EnumStatus=EnumStatus.OFFLINE
    var photo: String? = null
    var alwaysOnMap:Boolean = true
}


