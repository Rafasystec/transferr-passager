package br.com.transferr.util


import br.com.transferr.model.MapErroRetornoRest
import br.com.transferr.passenger.extensions.fromJson
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
                return ("Erro desconhecido: ${body.errorBody().toString()}")
            }
        }else if(code == HttpURLConnection.HTTP_UNAVAILABLE){
        }
        return "Desculpe! Sem retorno."
    }


}