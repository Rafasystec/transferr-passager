package br.com.transferr.passager.application

import android.support.multidex.MultiDexApplication

/**
 * Created by Rafael Rocha on 15/02/2018.
 */
class ApplicationTransferr : MultiDexApplication() {
    private val TAG = "APPLICATION"
    val URL_BASE = "http://192.168.1.7:8080/transferr-rest/rest/" //Windows
    //val URL_BASE = "http://192.168.15.7:8080/transferr-rest/rest/" //Linux
    //val URL_BASE = "http://petmooby.com.br/transferr-rest/rest/"
    override fun onCreate() {
        super.onCreate()
        //Salva a intancia para termos acesso como Sigleton
        appInstance = this
    }

    companion object {
        //Singleton da classe application
        private var appInstance: ApplicationTransferr? = null

        fun getInstance(): ApplicationTransferr {
            if (appInstance == null) {
                throw IllegalStateException("Configure the Application class on Manifest xml.")
            }
            return appInstance!!
        }
    }
}