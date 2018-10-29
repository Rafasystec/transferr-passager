package br.com.transferr.webservices


import br.com.transferr.model.Credentials
import br.com.transferr.model.responses.ResponseLogin
import br.com.transferr.model.responses.ResponseOK
import retrofit2.Call
import retrofit2.http.*

/**
 * Created by root on 20/02/18.
 */
interface IUserService {
    @POST("user/login")
    fun doLogin(@Body credentials: Credentials): Call<ResponseLogin>
    @PUT("user/change/password/{idUser}/{actualPassword}/{newpassword}")
    fun changePassword(@Path("idUser") idUser:Long,@Path("actualPassword")actualPassword:String,@Path("newpassword")newPassword:String ): Call<ResponseOK>
    @GET("user/recover/password/{email}")
    fun recoverPassword(@Path("email") email:String): Call<ResponseOK>
}