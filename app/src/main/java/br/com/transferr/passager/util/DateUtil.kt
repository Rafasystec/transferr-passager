package br.com.transferr.passager.util

import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Rafael Rocha on 31/08/2018.
 */
object DateUtil {
    @JvmStatic
    fun format(date: Date,format:String) : String {
        val format = SimpleDateFormat(format)
        return format.format(date)
    }
}