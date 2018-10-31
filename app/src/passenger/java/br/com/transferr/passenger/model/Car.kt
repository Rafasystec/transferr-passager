package br.com.transferr.model

import br.com.transferr.model.enums.EnumStatus
import java.util.*

/**
 * Created by Rafael Rocha on 08/02/2018.
 */
class Car : Entity(){
    var model: String=""
    var carIdentity: String=""
    var color:String=""
    var externalEquip:Boolean=false
    var driver:Driver=Driver("","",Date())
    var status:EnumStatus=EnumStatus.OFFLINE
    var photo: String? = null

}


