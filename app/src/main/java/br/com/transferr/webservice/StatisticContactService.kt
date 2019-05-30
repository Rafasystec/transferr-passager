package br.com.transferr.webservice

import br.com.transferr.helpers.HelperCallBackWebService
import br.com.transferr.model.StatisticContact
import br.com.transferr.model.responses.OnResponseInterface
import br.com.transferr.model.responses.ResponseOK
import br.com.transferr.webservices.SuperWebService
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Created by idoctor on 30/05/2019.
 */
const val BASE_URL_STATISTIC_CONTACT = "statistic"


object StatisticContactService : SuperWebService(){
    const val TAG = "STATISTIC"
    private var service: IStatisticService = StatisticContactService.retrofit.create(IStatisticService::class.java)
    fun sendContactStatistic(statisticContact:StatisticContact,responseInterface: OnResponseInterface<ResponseOK>){
        service.sendContactStatistic(statisticContact).enqueue(HelperCallBackWebService(responseInterface))
    }

}

interface IStatisticService{
    @POST("$BASE_URL_STATISTIC_CONTACT/send/contact")
    fun sendContactStatistic(@Body statisticContact:StatisticContact): Call<ResponseOK>
}