package br.com.transferr.webservices

import br.com.transferr.application.ApplicationTransferr
import br.com.transferr.helpers.HelperCallBackWebService
import br.com.transferr.model.AnexoPhoto
import br.com.transferr.model.Car
import br.com.transferr.model.Driver
import br.com.transferr.model.responses.OnResponseInterface
import br.com.transferr.model.responses.ResponseLogin
import br.com.transferr.model.responses.ResponseOK
import br.com.transferr.util.CallRESTMethodsUtil
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body

/**
 * Created by root on 12/02/18.
 */
object DriverService :SuperWebService(){
    private var service: IDriverService = retrofit.create(IDriverService::class.java)

    fun getDriver(id:Int,responseInterface: OnResponseInterface<Driver>) {
        return service.getDriver(id).enqueue(HelperCallBackWebService(responseInterface))
    }

    fun savePhoto(anexoPhoto: AnexoPhoto,responseInterface: OnResponseInterface<ResponseOK>){
        service.savePhoto(anexoPhoto).enqueue(HelperCallBackWebService(responseInterface))
    }

    fun getDriverByCar(id:Long,responseInterface: OnResponseInterface<Driver>){
        return service.getDriverByCar(id).enqueue(HelperCallBackWebService(responseInterface))
    }

    fun doGetByUserId(userId: Long,responseInterface: OnResponseInterface<Driver>){
        service.doGetByUserId(userId).enqueue(HelperCallBackWebService(responseInterface))
    }
}