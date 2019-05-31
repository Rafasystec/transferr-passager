package br.com.transferr.main.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import br.com.transferr.model.StatisticContact
import br.com.transferr.model.enums.EnumTypeContact

/**
 * Created by idoctor on 31/05/2019.
 */
object PhoneUtil {


    fun call(context:Context, phoneAsString:String,statisticContact: StatisticContact){
        statisticContact.type = EnumTypeContact.PHONE
        Thread({
            StatisticUtil.sendStatistic(statisticContact)
        }).start()
        call(context,phoneAsString)
    }
    @SuppressLint("MissingPermission")
    fun call(context:Context, phoneAsString:String){
        context!!.startActivity(Intent(Intent.ACTION_CALL, Uri.parse("tel:$phoneAsString")))
    }
}