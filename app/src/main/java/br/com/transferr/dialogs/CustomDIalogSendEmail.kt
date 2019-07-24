package br.com.transferr.dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.Window.FEATURE_NO_TITLE
import android.widget.Toast
import br.com.transferr.R
import br.com.transferr.main.util.Prefes
import br.com.transferr.model.Tourist
import br.com.transferr.model.enums.EnumDeviceType
import br.com.transferr.model.responses.OnResponseInterface
import br.com.transferr.webservice.TouristService
import kotlinx.android.synthetic.main.custom_dialog_send_email.*

class CustomDIalogSendEmail(context:Context) : Dialog(context) , View.OnClickListener{


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(FEATURE_NO_TITLE)
        setContentView(R.layout.custom_dialog_send_email)
        btnSend.setOnClickListener(this)
        btnNotNow.setOnClickListener(this)

    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.btnSend ->{
                sendInfor()
                this.dismiss()
            }
            R.id.btnNotNow ->{
                this.dismiss()
            }
        }

    }

    private fun sendInfor(){
        var txtEmail = edtSendEmail.text.toString()
        var txtDDD   = edtSendDDD.text.toString()
        var txtPhone = edtSendPhone.text.toString()
        if(txtEmail.isEmpty()){
            edtSendEmail.error = context.getString(R.string.pleaseTellUsYourEmail)
        }
        var tourist = Tourist()
        with(tourist){
            phone = txtDDD + txtPhone
            device = EnumDeviceType.ANDROID
            email   = txtEmail
        }
        Thread{
            TouristService.post(tourist,object : OnResponseInterface<Tourist>{
                override fun onSuccess(body: Tourist?) {
                    Toast.makeText(context,context.getString(R.string.emailSent),Toast.LENGTH_SHORT).show()
                }

            })
        }.start()
        Prefes.prefsIsEmailSent = true

    }


    companion object{
        fun show(context: Context){
            if(Prefes.prefsIsEmailSent)return
            val dialog = CustomDIalogSendEmail(context)
            dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()
        }
    }


}