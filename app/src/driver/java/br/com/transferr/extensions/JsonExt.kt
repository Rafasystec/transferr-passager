package br.com.transferr.extensions

import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken

/**
 * Created by idoctor on 08/02/2018.
 */
fun Any.toJson(prettyPrinting:Boolean = false):String{
    val builder = GsonBuilder()
    if(prettyPrinting){
        builder.setPrettyPrinting()
    }
    val json = builder.create().toJson(this)
    Log.d("JSON",json)
    return json
}

//Json to object
inline fun <reified T> Any.fromJson(json: String): T{
    val type = object : TypeToken<T>(){}.type
    return Gson().fromJson<T>(json,type)
}