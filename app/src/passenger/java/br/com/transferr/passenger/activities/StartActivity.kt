package br.com.transferr.passenger.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import br.com.transferr.R
import kotlinx.android.synthetic.passenger.activity_start.*
import org.jetbrains.anko.startActivity

class StartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        btnSeeNearDriver.setOnClickListener {
            //startActivity(Intent(this, MainActivity::class.java))
            startActivity<br.com.transferr.passenger.activities.MapsActivity>()
        }
        btnSeeTours.setOnClickListener {
            //startActivity(Intent(this,TourListActivity::class.java))
            startActivity<br.com.transferr.passenger.activities.TourListActivity>()
        }
        btnExit.setOnClickListener {
            this.finish()
            System.exit(0)
        }
        //setupToolbar(R.id.toolbar,"Escolha uma opção.")
    }


}
