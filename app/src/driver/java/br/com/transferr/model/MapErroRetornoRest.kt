package br.com.transferr.model

import br.com.transferr.model.enums.EnumFailSystem

/**
 * Created by root on 14/02/18.
 */
class MapErroRetornoRest {

    var type: EnumFailSystem? = null
    var message: String? = null
    var rootCause: String? = null
    var rootMessage: String? = null
    var fields: Array<String>? = null
}