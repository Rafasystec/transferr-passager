package br.com.transferr.passenger.util

import android.app.Application
import br.com.transferr.application.ApplicationTransferr
import br.com.transferr.model.enums.EnumLanguage
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by Rafael Rocha on 31/08/2018.
 */
object DateUtil {

    @JvmStatic
    val DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ"

    @JvmStatic
    fun format(date: Date,format:String) : String {
        val format = SimpleDateFormat(format)
        return format.format(date)
    }

    fun addDaysForADate(date: Date, days: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.DAY_OF_YEAR, days)
        return calendar.time
    }
    @JvmStatic
    fun getDateDescription(dateTimeParam: Date, showTime:Boolean): String {
        var dateTime = dateTimeParam
        val APPLICATION_FORMAT_INPUT = "ddMMyyyy"
        dateTime = getOnlyDate(dateTime, APPLICATION_FORMAT_INPUT)
        return if (dateTime.before(getOnlyDate(getDateTimeNow(), APPLICATION_FORMAT_INPUT))) {
            "Ontem, " + getDayOfTheWeekExtention(dateTime) + " - " + DateUtil.showTime(dateTimeParam,showTime)
        } else if (dateTime.equals(getOnlyDate(getDateTimeNow(), APPLICATION_FORMAT_INPUT))) {
            "Hoje, " + getDayOfTheWeekExtention(dateTime) + " - " + DateUtil.showTime(dateTimeParam,showTime)
        } else if (dateTime.equals(getOnlyDate(addDaysForADate(getDateTimeNow(), 1), APPLICATION_FORMAT_INPUT))) {
            "Amanhã, " + getDayOfTheWeekExtention(dateTime) + " - " + DateUtil.showTime(dateTimeParam,showTime)
        } else if (dateTime.equals(getOnlyDate(addDaysForADate(getDateTimeNow(), 2), APPLICATION_FORMAT_INPUT))) {
            getDayOfTheWeekExtention(dateTime) + " - dia " + getDayOfTheMonth(dateTime) + "-" + DateUtil.showTime(dateTimeParam,showTime)
        } else if (dateTime.after(getOnlyDate(addDaysForADate(getDateTimeNow(), 2), APPLICATION_FORMAT_INPUT))) {
            getDayOfTheWeekExtention(dateTime) + " - dia " + getDayOfTheMonth(dateTime)+ "-" + DateUtil.showTime(dateTimeParam,showTime)
        } else {
            getDayOfTheWeekExtention(dateTime) + " - dia " + getDayOfTheMonth(dateTime)+ "-" + DateUtil.showTime(dateTimeParam,showTime)
        }
    }
    @JvmStatic
    fun getOnlyDate(date: Date, formatOut: String): Date {
        val sdf = SimpleDateFormat(formatOut)
        var dateWithoutTime: Date
        try {
            dateWithoutTime = sdf.parse(sdf.format(date))
        } catch (e: ParseException) {
            return date
        }

        val cal = Calendar.getInstance()
        cal.time = date
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        dateWithoutTime = cal.time
        return dateWithoutTime
    }

    @JvmStatic
    fun getDayOfTheWeekExtention(date: Date): String {
        val cal = Calendar.getInstance()
        cal.time = date
        val day = cal.get(Calendar.DAY_OF_WEEK)
        when (day) {
            1 -> return "Domingo"
            2 -> return "Segunda-feira"
            3 -> return "Terça-feira"
            4 -> return "Quarta-feira"
            5 -> return "Quinta-feira"
            6 -> return "Sexta-feira"
            7 -> return "Sábado"
            else -> return ""
        }
    }

    @JvmStatic
    fun getDateTimeNow(): Date {
        return Date()
    }
    @JvmStatic
    fun getDayOfTheMonth(date: Date): Int {
        val cal = Calendar.getInstance()
        cal.time = date
        return cal.get(Calendar.DAY_OF_MONTH)
    }

    fun showTime(date:Date,showTime: Boolean) : String{
        return if(showTime){
           format(date,"HH:mm")
        }else{
            ""
        }
    }

    @JvmStatic
    fun toDate(stringDate:String,formatInput:String,formatOut: String = "yyyy-MM-dd'T'HH:mm:ssZ"): Date{
        val formatter = SimpleDateFormat (formatInput)
        var date    = formatter.parse(stringDate)
        val strFormatedDate = SimpleDateFormat(formatOut).format(date)
        return SimpleDateFormat(formatOut).parse(strFormatedDate)
    }

    @JvmStatic
    fun getDateByLanguageAsString(date:Date,enumLanguage: EnumLanguage): String{
        return when(enumLanguage){
            EnumLanguage.ES_ES -> {
                return format(date,"dd/MM/yyyy HH:mm")
            }EnumLanguage.PT_BR ->{
                return format(date,"dd/MM/yyyy HH:mm")
            }else->{
                return format(date,"MM/dd/yyyy hh:mm a")
            }
        }
    }

    @JvmStatic
    fun getDateByLanguageAsString(map:Map<EnumLanguage,String>): String{
        return when(ApplicationTransferr.DEVICE_LANGUAGE){
            ApplicationTransferr.LANG_ES -> {
                return map[EnumLanguage.ES_ES].toString()
            }ApplicationTransferr.LANG_PT ->{
                return map[EnumLanguage.PT_BR].toString()
            }else->{
                return map[EnumLanguage.EN_USA].toString()
            }
        }
    }

    @JvmStatic
    fun getDateByLanguageAsString(date:Date): String{
        return when(ApplicationTransferr.DEVICE_LANGUAGE){
            ApplicationTransferr.LANG_PT -> {
                return format(date,"dd/MM/yyyy HH:mm")
            }ApplicationTransferr.LANG_ES ->{
                return format(date,"dd/MM/yyyy HH:mm")
            }else->{
                return format(date,"MM/dd/yyyy hh:mm a")
            }
        }
    }

}