package br.com.transferr.passager.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import br.com.transferr.passager.R
import br.com.transferr.passager.extensions.setupToolbar
import kotlinx.android.synthetic.main.activity_start.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class StartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        btnSeeNearDriver.setOnClickListener {
            //startActivity(Intent(this, MainActivity::class.java))
            startActivity<MainActivity>()
        }
        btnSeeTours.setOnClickListener {
            //startActivity(Intent(this,TourListActivity::class.java))
            startActivity<TourListActivity>()
        }
        btnExit.setOnClickListener {
            this.finish()
            System.exit(0)
        }
        setupToolbar(R.id.toolbar,"Escolha uma opção.")
    }


}
