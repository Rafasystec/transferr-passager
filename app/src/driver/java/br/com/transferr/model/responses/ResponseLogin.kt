package br.com.transferr.model.responses

import br.com.transferr.model.User

/**
 * Created by root on 20/02/18.
 */
class ResponseLogin : SuperResponse(){
    var user: User? = null
    var token: String? = null
}