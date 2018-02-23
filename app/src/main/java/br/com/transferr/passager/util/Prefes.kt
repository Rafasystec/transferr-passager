package br.com.transferr.util

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color

/**
 * Created by root on 12/02/18.
 */
class Prefes (context: Context) {
    val PREFS_FILENAME = "br.com.transferr.prefs"
    val ID_DRIVER = "br.com.transferr.driver.id"
    val ID_DEVICE = "br.com.transferr.device.id"
    val prefs: SharedPreferences = context.getSharedPreferences(PREFS_FILENAME, 0)

    var prefsLogin: Int
        get() = prefs.getInt(ID_DRIVER, 0)
        set(value) = prefs.edit().putInt(ID_DRIVER, value).apply()

    var prefsToken: String
        get() = prefs.getString(ID_DEVICE, "")
        set(value) = prefs.edit().putString(ID_DEVICE, value).apply()
}