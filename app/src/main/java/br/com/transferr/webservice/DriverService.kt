package br.com.transferr.webservices

import android.app.ProgressDialog
import android.support.v4.app.FragmentActivity
import android.support.v7.app.AppCompatActivity
import br.com.transferr.helpers.HelperCallBackWebService
import br.com.transferr.model.AnexoPhoto
import br.com.transferr.model.Driver
import br.com.transferr.model.responses.OnResponseInterface
import br.com.transferr.model.responses.ResponseOK

/**
 * Created by Rafael Rocha on 12/02/18.
 */
object DriverService :SuperWebService(){
    private var service: IDriverService = retrofit.create(IDriverService::class.java)

    fun getDriver(id:Int,responseInterface: OnResponseInterface<Driver>) {
        return service.getDriver(id).enqueue(HelperCallBackWebService(responseInterface))
    }

    fun savePhoto(anexoPhoto: AnexoPhoto, responseInterface: OnResponseInterface<ResponseOK>){
        service.savePhoto(anexoPhoto).enqueue(HelperCallBackWebService(responseInterface))
    }

    fun getDriverByCar(id:Long,responseInterface: OnResponseInterface<Driver>){
        return service.getDriverByCar(id).enqueue(HelperCallBackWebService(responseInterface))
    }

    fun doGetByUserId(userId: Long,responseInterface: OnResponseInterface<Driver>,appCompatActivity: FragmentActivity?,progress:ProgressDialog){
        service.doGetByUserId(userId).enqueue(HelperCallBackWebService(responseInterface,appCompatActivity,progress = progress))
    }
}