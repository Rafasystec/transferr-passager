package br.com.transferr.webservices


import br.com.transferr.helpers.HelperCallBackWebService
import br.com.transferr.model.Credentials
import br.com.transferr.model.responses.OnResponseInterface
import br.com.transferr.model.responses.ResponseLogin
import br.com.transferr.model.responses.ResponseOK
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by Rafael Rocha on 20/02/18.
 */
object UserService :SuperWebService(){

    private var service: IUserService
    init {
        val retrofit = Retrofit.Builder()
                .baseUrl(UserService.urlBase)
                .addConverterFactory(GsonConverterFactory.create())
                .client(UserService.httpClient)
                .build()
        service = retrofit.create(IUserService::class.java)

    }

    fun doLogin(credentials: Credentials,responseInterface: OnResponseInterface<ResponseLogin>){

        service.doLogin(credentials).enqueue(
                HelperCallBackWebService(responseInterface)
        )


    }


    fun changePassword(idUser:Long, actualPassword:String, newPassword:String,responseInterface: OnResponseInterface<ResponseOK> ){
        return service.changePassword(idUser,actualPassword,newPassword).enqueue(HelperCallBackWebService(responseInterface))
    }

    fun recoverPassword(email:String,responseInterface: OnResponseInterface<ResponseOK>){
        return service.recoverPassword(email).enqueue(HelperCallBackWebService(responseInterface))
    }

}