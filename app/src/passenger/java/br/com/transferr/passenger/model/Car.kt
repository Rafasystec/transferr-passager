package br.com.transferr.model

import br.com.transferr.model.enums.EnumStatus

/**
 * Created by Rafael Rocha on 08/02/2018.
 */
class Car : Entity(){
    var model: String=""
    var carIdentity: String=""
    var color:String=""
    var externalEquip:Boolean=false
    var driver:Driver=Driver("","",0)
    var status:EnumStatus=EnumStatus.OFFLINE
    var photo: String? = null

}


