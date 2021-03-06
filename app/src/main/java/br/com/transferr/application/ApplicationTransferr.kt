package br.com.transferr.application

import android.support.annotation.StringRes
import android.support.multidex.MultiDexApplication


import java.util.*

/**
 * Created by Rafael Rocha on 15/02/2018.
 */
class ApplicationTransferr : MultiDexApplication() {
    private val TAG = "APPLICATION"
    //val URL_BASE = "http://192.168.1.8:8080/rest/" //Windows
//    val URL_BASE = "http://192.168.0.115:8080/transferr-rest/rest/" //Linux
    val URL_BASE = "https://petmooby.com.br/transferr-rest/rest/"
    override fun onCreate() {
        super.onCreate()
        //Salva a intancia para termos acesso como Sigleton
        br.com.transferr.application.ApplicationTransferr.Companion.appInstance = this
    }

    companion object {
        //Singleton da classe application
        private var appInstance: br.com.transferr.application.ApplicationTransferr? = null
        var DEVICE_LANGUAGE = Locale.getDefault().language
        val LANG_PT = "pt"
        val LANG_EN = "en"
        val LANG_ES = "es"
        fun getInstance(): br.com.transferr.application.ApplicationTransferr {
            if (br.com.transferr.application.ApplicationTransferr.Companion.appInstance == null) {
                throw IllegalStateException("Configure the Application class on Manifest xml.")
            }
            return br.com.transferr.application.ApplicationTransferr.Companion.appInstance!!
        }

        fun getString(@StringRes resId:Int ) : String{
            return appInstance?.getString(resId)!!
        }
    }




}