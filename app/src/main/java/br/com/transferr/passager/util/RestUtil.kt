package br.com.transferr.passager.util

import br.com.transferr.model.MapErroRetornoRest
import br.com.transferr.passager.extensions.fromJson
import retrofit2.Response
import java.net.HttpURLConnection

/**
 * Created by Rafael Rocha on 27/07/2018.
 */
class RestUtil<T> {
    fun getErroMessage(body: Response<T>) : String{
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