package br.com.transferr.activities

import android.os.Bundle
import android.view.View
import br.com.transferr.R
import br.com.transferr.extensions.showError
import br.com.transferr.extensions.showValidation
import br.com.transferr.extensions.toast
import br.com.transferr.model.Credentials
import br.com.transferr.model.responses.OnResponseInterface
import br.com.transferr.model.responses.ResponseLogin
import br.com.transferr.model.responses.ResponseOK
import br.com.transferr.main.util.Prefes
import br.com.transferr.model.Driver
import br.com.transferr.passenger.extensions.showLoadingDialog
import br.com.transferr.webservices.DriverService
import br.com.transferr.webservices.UserService
import kotlinx.android.synthetic.driver.activity_login.*

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
            initProgressBar()
            UserService.doLogin(getCredentialsFromForm(),
                object : OnResponseInterface<ResponseLogin>{
                    override fun onSuccess(body: ResponseLogin?) {
                        stopProgressBar()
                        Prefes.prefsLogin = body?.user?.id!!
                        //executeLogin(body?.user?.id!!)
                        getDriverFromWebService()
                    }
                    override fun onError(message: String) {
                        stopProgressBar()
                        toast(message)
                    }
                    override fun onFailure(t: Throwable?) {
                        stopProgressBar()
                        toast(t?.message!!)
                    }

                }
            )


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
           initProgressBar()
           var email = txtLogin.text.toString().trim()
           UserService.recoverPassword(email,
               object : OnResponseInterface<ResponseOK>{
                   override fun onSuccess(body: ResponseOK?) {
                       stopProgressBar()
                       toast("Um e-mail foi enviado para $email")
                   }

                   override fun onError(message: String) {
                       stopProgressBar()
                       showValidation(message)
                   }

                   override fun onFailure(t: Throwable?) {
                       stopProgressBar()
                       showError(t?.message!!)
                   }

               }
           )
       }

    }

    private fun initProgressBar(){
        this@LoginActivity.runOnUiThread({
            progressBar.visibility = View.VISIBLE
        })
    }

    private fun stopProgressBar(){
        this@LoginActivity.runOnUiThread({
            progressBar.visibility = View.GONE
        })
    }

    private fun getDriverFromWebService(){
        var progress = showLoadingDialog(message = getString(R.string.getTheDriver))
        DriverService.doGetByUserId(Prefes.prefsLogin,
                object: OnResponseInterface<Driver> {
                    override fun onSuccess(driver: Driver?) {
                        progress.dismiss()
                        Prefes.driver = driver!!
                        executeLogin()
                    }

                    override fun onError(message: String) {
                        progress.dismiss()

                    }

                    override fun onFailure(t: Throwable?) {
                        progress.dismiss()

                    }

                }
        )

    }
}


