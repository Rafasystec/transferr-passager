package br.com.transferr.webservices

import br.com.transferr.helpers.HelperCallBackWebService
import br.com.transferr.model.Car
import br.com.transferr.model.responses.OnResponseInterface
import br.com.transferr.model.responses.RequestCoordinatesUpdate
import br.com.transferr.model.responses.ResponseCarOnline
import br.com.transferr.model.responses.ResponseOK
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by root on 12/02/18.
 */
object CarService : SuperWebService(){
    private var service: ICarService
    init {
        val retrofit = Retrofit.Builder()
                .baseUrl(urlBase)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .build()
        service = retrofit.create(ICarService::class.java)
    }

    fun getCar(id:Long,responseInterface: OnResponseInterface<Car>) {
        service.getCar(id).enqueue(HelperCallBackWebService(responseInterface))

    }

    fun getCarByUser(id:Long,responseInterface: OnResponseInterface<Car>) {
        service.getCarByUser(id).enqueue(HelperCallBackWebService(responseInterface))
    }

    fun online(requestCoordinatesUpdate: RequestCoordinatesUpdate,responseInterface: OnResponseInterface<ResponseOK>){
        service.online(requestCoordinatesUpdate).enqueue(HelperCallBackWebService(responseInterface))
    }

    fun offline(requestCoordinatesUpdate: RequestCoordinatesUpdate,responseInterface: OnResponseInterface<ResponseOK>){
        service.offline(requestCoordinatesUpdate).enqueue(HelperCallBackWebService(responseInterface))
    }

    fun changeAlwaysParameter(idCar:Long, always:Boolean,responseInterface: OnResponseInterface<ResponseOK>){
        service.doPutAlwaysParameter(idCar,always).enqueue(HelperCallBackWebService(responseInterface))
    }

    fun updateStatus(responseCarOnline: ResponseCarOnline,responseInterface: OnResponseInterface<ResponseOK>){
        service.doPutCarStatus(responseCarOnline).enqueue(HelperCallBackWebService(responseInterface))
    }


}