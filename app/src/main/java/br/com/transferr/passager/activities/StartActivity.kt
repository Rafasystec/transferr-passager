package br.com.transferr.passager.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import br.com.transferr.passager.R
import kotlinx.android.synthetic.main.activity_start.*
import org.jetbrains.anko.startActivity

class StartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        btnSeeNearDriver.setOnClickListener {
            //startActivity(Intent(this, MainActivity::class.java))
            startActivity<MapsActivity>()
        }
        btnSeeTours.setOnClickListener {
            //startActivity(Intent(this,TourListActivity::class.java))
            startActivity<TourListActivity>()
        }
        btnExit.setOnClickListener {
            this.finish()
            System.exit(0)
        }
        //setupToolbar(R.id.toolbar,"Escolha uma opção.")
    }


}
