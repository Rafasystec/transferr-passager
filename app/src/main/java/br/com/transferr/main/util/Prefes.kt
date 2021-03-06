package br.com.transferr.main.util

import android.content.SharedPreferences
import br.com.transferr.application.ApplicationTransferr
import br.com.transferr.model.Car
import br.com.transferr.model.Driver
import br.com.transferr.passenger.extensions.fromJson
import br.com.transferr.passenger.extensions.toJson

/**
 * Created by root on 12/02/18.
 */
object Prefes {
    val PREFS_FILENAME   = "br.com.transferr.prefs"
    val ID_DRIVER        = "br.com.transferr.driver.id"
    val ID_USER          = "br.com.transferr.user.id"
    val ID_CAR           = "br.com.transferr.car.id"
    val ID_CAR_JSON      = "br.com.transferr.car.id.json"
    val ID_DRIVER_JSON   = "br.com.transferr.car.id.json"
    val SENT_EMAIL       = "br.com.transferr.car.id.json"

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
    var driver: Driver
        get() = fromJson(prefs().getString(ID_DRIVER_JSON,""))
        set(driver) = prefs().edit().putString(ID_DRIVER_JSON,driver.toJson()).apply()

    //Use to save other values
    fun setInt(flag:String,value:Int) = prefs().edit().putInt(flag,value).apply()
    fun getInt(flag: String)= prefs().getInt(flag,0)
    fun setString(flag: String,value:String) = prefs().edit().putString(flag,value).apply()
    fun clear(){
       prefs().edit().clear().commit()
    }
    fun setBoolean(flag: String,value:Boolean) = prefs().edit().putBoolean(flag,value).apply()
    var prefsIsEmailSent: Boolean
        get() = prefs().getBoolean(SENT_EMAIL,true)
        set(value) = setBoolean(SENT_EMAIL,value)

}