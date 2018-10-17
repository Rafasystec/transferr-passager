package br.com.transferr.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import br.com.transferr.R
import kotlinx.android.synthetic.main.activity_terms_of_use.*

class TermsOfUseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terms_of_use)
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.termsOfUseTitle)
        btnCloseTermOfUse.setOnClickListener {
            this.finish()
        }
    }

}
