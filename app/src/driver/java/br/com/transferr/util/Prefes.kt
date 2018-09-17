package br.com.transferr.util

import android.content.SharedPreferences
import br.com.transferr.application.ApplicationTransferr
import br.com.transferr.extensions.fromJson
import br.com.transferr.extensions.toJson
import br.com.transferr.model.Car

/**
 * Created by root on 12/02/18.
 */
object Prefes {
    val PREFS_FILENAME   = "br.com.transferr.prefs"
    val ID_DRIVER        = "br.com.transferr.driver.id"
    val ID_USER          = "br.com.transferr.user.id"
    val ID_CAR           = "br.com.transferr.car.id"
    val ID_CAR_JSON      = "br.com.transferr.car.id.json"

    private fun prefs() : SharedPreferences{
        val contex = ApplicationTransferr.getInstance().applicationContext
        return contex.getSharedPreferences(PREFS_FILENAME,0)
    }
    var prefsLogin: Long
        get() = prefs().getLong(ID_USER,0L)
        set(value) = prefs().edit().putLong(ID_USER, value).apply()
    var prefsDriver: Long
        get() = prefs().getLong(ID_DRIVER,0L)
        set(value) = prefs().edit().putLong(ID_DRIVER, value).apply()
    var prefsCar: Long
        get() = prefs().getLong(ID_CAR,0L)
        set(value) = prefs().edit().putLong(ID_CAR, value).apply()
    var prefsCarJSON: Car
        get() = fromJson(prefs().getString(ID_CAR_JSON,""))
        set(car) = prefs().edit().putString(ID_CAR_JSON,car.toJson()).apply()

    //Use to save other values
    fun setInt(flag:String,value:Int) = prefs().edit().putInt(flag,value).apply()
    fun getInt(flag: String)= prefs().getInt(flag,0)
    fun setString(flag: String,value:String) = prefs().edit().putString(flag,value).apply()

}