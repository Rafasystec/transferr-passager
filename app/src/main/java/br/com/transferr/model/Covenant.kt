package br.com.transferr.model

import br.com.transferr.model.enums.EnumCategory

const val COVENANT_PARAM_NAME = "Covenant"

class Covenant : Entity(){


    var description: String? = null
    var category: EnumCategory? = null
    var urlLogo: String? = null
    var message: String? = null
    var active: Boolean = false
    //var title : String? = null
}