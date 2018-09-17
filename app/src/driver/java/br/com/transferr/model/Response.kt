package br.com.transferr.model

/**
 * Created by idoctor on 08/02/2018.
 */
data class Response(val id:Long,val status:String,val msg:String,val url:String) {
    fun isOK() = "OK".equals(status,ignoreCase = true)
}