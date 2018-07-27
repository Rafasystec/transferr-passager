package br.com.transferr.passager.webservices

import br.com.transferr.passager.application.ApplicationTransferr
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by Rafael Rocha on 27/07/2018.
 */
open class BaseWebService {
    protected var urlBase = ApplicationTransferr.getInstance().URL_BASE
    protected var httpClient: OkHttpClient = OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .writeTimeout(5, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            .build()

    protected val retrofit = Retrofit.Builder()
            .baseUrl(urlBase)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient)
            .build()


}