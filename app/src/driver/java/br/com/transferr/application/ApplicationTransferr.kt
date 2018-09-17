package br.com.transferr.application

import android.app.Application

/**
 * Created by Rafael Rocha on 07/02/2018.
 */
class ApplicationTransferr : Application() {
    private val TAG = "APPLICATION"
    //private val base = "http://192.168.15.7:8080/"    //For Linux
    private val base = "http://192.168.1.19:8080/" //For windows
    //private val base = "http://petmooby.com.br/"
    val URL_BASE        = base +"transferr-rest/rest/"
    //val URL_BASE        = base +"rest/" //For hospedagem
    val URL_BASE_IMG    = base +"files/"
    override fun onCreate() {
        super.onCreate()
        appInstance = this
    }

    companion object {
        private var appInstance: ApplicationTransferr? = null
        fun getInstance(): ApplicationTransferr{
            if (appInstance == null){
                throw IllegalStateException("Configure the Application class on Manifest xml.")
            }
            return appInstance!!
        }
    }

}