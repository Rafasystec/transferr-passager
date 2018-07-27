package br.com.transferr.passager.activities

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import br.com.transferr.passager.util.AlertUtil
import org.jetbrains.anko.alert
import org.jetbrains.anko.okButton

/**
 * Created by root on 14/02/18.
 */
@SuppressLint("Registered")
open class SuperClassActivity : AppCompatActivity(){
    //Propriedade para acessar o contexto de qualquer lugar
    protected val context: Context get() = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("DEBUG","setSupportActionBar")
        //setSupportActionBar(toolbar)
    }

    protected fun alertWarning(message: String){
        this!!.alert ( message, AlertUtil.DEFAULT_VALIDATE_TITLE){
            okButton { it.dismiss() }
        }.show()
    }

    protected fun alertErro(message: String){
        this!!.alert ( message,AlertUtil.DEFAULT_ERROR_TITLE){
            okButton { it.dismiss() }
        }.show()
    }

}