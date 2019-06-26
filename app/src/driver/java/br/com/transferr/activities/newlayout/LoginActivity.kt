package br.com.transferr.activities

import android.content.Intent
import android.os.Bundle
import br.com.transferr.R
import br.com.transferr.activities.newlayout.MainActivity
import br.com.transferr.main.util.Prefes
import br.com.transferr.model.Credentials
import br.com.transferr.model.Driver
import br.com.transferr.model.responses.OnResponseInterface
import br.com.transferr.model.responses.ResponseLogin
import br.com.transferr.model.responses.ResponseOK
import br.com.transferr.passenger.extensions.showAlert
import br.com.transferr.passenger.extensions.showAlertError
import br.com.transferr.passenger.extensions.showLoadingDialog
import br.com.transferr.passenger.util.WhatsAppUtil
import br.com.transferr.webservices.DriverService
import br.com.transferr.webservices.UserService
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.toast

class LoginActivity : SuperClassActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        btnRegister.setOnClickListener{
            requestLogin()
        }
        btnRecoverPass.setOnClickListener{
            callServiceToRecoverPassword()
        }

        btnWannaRegister.setOnClickListener {
            WhatsAppUtil.callWhatsapp("+5585985635021",this,"Olá! Quero me cadastrar no BOORA! ")
        }
        checkUserLogin()
    }

    private fun validate() : Boolean{
        if(txtLogin.text.toString().trim().isEmpty()){
            toast("Por favor informe o usuário")
            return false
        }
        if(txtPassword.text.toString().trim().isEmpty()){
            toast("Por favor informe a senha!")
            return false
        }

        return true
    }

    private fun requestLogin(){
        if(validate()){
            var alert = showLoadingDialog(message = "Efetuando o Login. Aguarde!")
            UserService.doLogin(getCredentialsFromForm(),
                object : OnResponseInterface<ResponseLogin>{
                    override fun onSuccess(body: ResponseLogin?) {
                        alert.dismiss()
                        Prefes.prefsLogin = body?.user?.id!!
                        getDriverFromWebService()
                    }

                }
            ,this,alert)


        }

    }

    private fun checkUserLogin(){
        val id = Prefes.prefsLogin
        if(id != null && id > 0L){
            callMainActivity()
            finish()
        }
    }

    private fun callMainActivity(){
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun getCredentialsFromForm():Credentials{
        return Credentials(txtLogin.text.toString(),txtPassword.text.toString())
    }

    private fun executeLogin(){
        callMainActivity()
    }

    private fun validateRecoverPasswor():Boolean{
        var email = txtLogin.text.toString().trim()
        if(email.isEmpty()) {
            toast("Por favor informe o Usuário, ele é o seu e-mail.")
            return false
        }
        return true
    }

    private fun callServiceToRecoverPassword(){
       if(validateRecoverPasswor()){
           var dialog = showLoadingDialog(R.string.recoveringPassWord)
           var email = txtLogin.text.toString().trim()
           UserService.recoverPassword(email,
               object : OnResponseInterface<ResponseOK>{
                   override fun onSuccess(body: ResponseOK?) {
                       dialog.dismiss()
                       showAlert("Um e-mail foi enviado para $email")
                   }

               }
           ,this,dialog)
       }

    }

    private fun getDriverFromWebService(){
        val progress = showLoadingDialog(message = getString(R.string.getTheDriver))
        DriverService.doGetByUserId(Prefes.prefsLogin,
                object: OnResponseInterface<Driver> {
                    override fun onSuccess(driver: Driver?) {
                        progress.dismiss()
                        Prefes.driver = driver!!
                        Prefes.prefsCar = driver.car?.id!!
                        executeLogin()
                    }

                }
        ,this,progress)

    }

    override fun onBackPressed() {
        super.onBackPressed()
        this.finishAffinity()
    }
}


