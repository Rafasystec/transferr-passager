package br.com.transferr.model

import br.com.transferr.model.enums.EnumStatus

/**
 * Created by Rafael Rocha on 08/02/2018.
 */
data class Car(var model: String,
               var carIdentity: String,
               var color:String,
                var externalEquip:Boolean,
               var driver:Driver,
               var status:EnumStatus) : Entity(){
    var photo: String? = null

}


