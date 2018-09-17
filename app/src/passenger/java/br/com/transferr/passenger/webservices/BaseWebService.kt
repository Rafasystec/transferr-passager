package br.com.transferr.passenger.webservices

import android.widget.Toast
import br.com.transferr.passenger.application.ApplicationTransferr
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit

/**
 * Created by Rafael Rocha on 27/07/2018.
 */
open class BaseWebService {


    protected var urlBase = br.com.transferr.passenger.application.ApplicationTransferr.getInstance().URL_BASE
    protected var httpClient: OkHttpClient = OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .writeTimeout(5, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            /*
            .addInterceptor({ chain ->

                //var request:Request? = null
                try {
                    //request = chain?.request()?.newBuilder()?.build()
                    chain.proceed(chain.request())
                }catch (socket: SocketTimeoutException) {
                    Toast.makeText(ApplicationTransferr.getInstance().baseContext,"Servidor demorou muito pra responder",Toast.LENGTH_LONG).show()
                }
                chain?.proceed(chain.request())
            })
            */
            .build()


    protected val retrofit = Retrofit.Builder()
            .baseUrl(urlBase)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient)
            .build()


}