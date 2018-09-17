package br.com.transferr.util

import br.com.transferr.extensions.fromJson
import br.com.transferr.extensions.toast
import br.com.transferr.model.MapErroRetornoRest
import okhttp3.ResponseBody
import retrofit2.Response
import java.net.HttpURLConnection

/**
 * Created by root on 21/02/18.
 */
class RestUtil<T> {

    fun getErroMessage(body:Response<T>) : String{
        var code = body.code()
        if(code == HttpURLConnection.HTTP_BAD_REQUEST ||
                code == HttpURLConnection.HTTP_INTERNAL_ERROR ) {
            //Treat validation erro
            try{
                var json = body.errorBody()?.string()
                var mapErro = fromJson<MapErroRetornoRest>(json!!)
                return mapErro.message!!
            }catch (e:Exception){
                return ("Erro deconhecido ${body.errorBody().toString()}")
            }
        }
        return "Desculpe! Sem retorno."
    }


}