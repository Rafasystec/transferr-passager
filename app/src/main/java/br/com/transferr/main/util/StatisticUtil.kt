package br.com.transferr.main.util

import android.content.Context
import android.util.Log
import br.com.transferr.model.StatisticContact
import br.com.transferr.model.enums.EnumDeviceType
import br.com.transferr.model.responses.OnResponseInterface
import br.com.transferr.model.responses.ResponseOK
import br.com.transferr.webservice.StatisticContactService
import com.google.android.gms.maps.model.LatLng
import java.util.*

/**
 * Created by idoctor on 30/05/2019.
 */
object StatisticUtil {
    fun sendStatistic(statisticContact: StatisticContact) {
        try {
            StatisticContactService.sendContactStatistic(statisticContact, object : OnResponseInterface<ResponseOK> {
                override fun onSuccess(body: ResponseOK?) {
                    Log.d(StatisticContactService.TAG, body?.descricao)
                }
                override fun onError(message: String) {
                    Log.d(StatisticContactService.TAG, message!!)
                }
                override fun onFailure(t: Throwable?) {
                    Log.d(StatisticContactService.TAG, "Error", t!!)
                }

            })
        } catch (e: Exception) {
        }
    }

    fun getStatistic(idDriver:Long, context: Context): StatisticContact {
        var statistic = StatisticContact()
        var latLong = GPSUtil.getLocation(context)
        if(latLong == null){
            latLong = LatLng(0.toDouble(),0.toDouble())
        }
        with(statistic) {
            date        = Date()
            device      = EnumDeviceType.ANDROID
            this.idDriver = idDriver
            latitude    = latLong.latitude!!
            longitude   = latLong.longitude!!
        }
        return statistic
    }

}