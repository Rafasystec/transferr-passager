package br.com.transferr.model

/**
 * Created by root on 06/02/18.
 */
class Credentials(login:String, password:String) {

    val login:String
    val password:String

    init {
        this.login      = login
        this.password   = password
    }

    override fun toString(): String {
        return "Credentials login is $login"
    }

}