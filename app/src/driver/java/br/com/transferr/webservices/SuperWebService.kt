package br.com.transferr.webservices

import android.content.Context
import br.com.transferr.application.ApplicationTransferr
import br.com.transferr.util.NetworkUtil
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by root on 16/02/18.
 */
open class SuperWebService {
    protected var urlBase = ApplicationTransferr.getInstance().URL_BASE
    protected var httpClient:OkHttpClient = OkHttpClient.Builder()
            .connectTimeout(5,TimeUnit.SECONDS)
            .writeTimeout(5,TimeUnit.SECONDS)
            .readTimeout(15,TimeUnit.SECONDS)
            .build()

    protected val retrofit = Retrofit.Builder()
            .baseUrl(urlBase)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient)
            .build()

    protected fun isConnected():Boolean{
        return NetworkUtil.isNetworkAvailable(ApplicationTransferr.getInstance().applicationContext)
    }
}