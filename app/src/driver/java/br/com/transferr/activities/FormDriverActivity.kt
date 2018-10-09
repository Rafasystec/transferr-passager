package br.com.transferr.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import br.com.transferr.R
import kotlinx.android.synthetic.driver.activity_form_driver.*
import org.jetbrains.anko.toast

class FormDriverActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_driver)
        loadFields()
        btnSaveDriver.setOnClickListener { save() }
    }

    private fun loadFields(){
        txtDtNacimento.setText("05/03/1988")
        txtName.setText("João das Cuias")
    }

    private fun clearFields(){
        txtDtNacimento.text.clear()
        txtName.text.clear()
    }

    private fun save(){
        toast("Salvo ${txtName.text}")
    }
}
