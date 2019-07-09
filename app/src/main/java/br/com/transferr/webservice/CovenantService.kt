package br.com.transferr.webservice

import android.app.ProgressDialog
import android.support.v4.app.FragmentActivity
import br.com.transferr.helpers.HelperCallBackWebService
import br.com.transferr.model.Covenant
import br.com.transferr.model.responses.OnResponseInterface
import br.com.transferr.webservices.SuperWebService
import retrofit2.Call
import retrofit2.http.GET

const val BASE_URL_COVENANT = "covenant"
object CovenantService : SuperWebService() {
    private var service : ICovenantService = CovenantService.retrofit.create(ICovenantService::class.java)
    fun getActives(responseInterface: OnResponseInterface<List<Covenant>>, activity: FragmentActivity, progress: ProgressDialog?){
        service.getActives().enqueue(HelperCallBackWebService(responseInterface, activity,progress))
    }
}

interface ICovenantService{
    @GET(BASE_URL_COVENANT+"/actives")
    fun getActives(): Call<List<Covenant>>
}