package br.com.transferr.passager.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import br.com.transferr.passager.R
import br.com.transferr.passager.extensions.setupToolbar
import br.com.transferr.passager.fragments.TourOptionLisFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupToolbar(R.id.toolbar,R.string.home)
        includeFragment()

    }


    private fun includeFragment(){
        val fm = supportFragmentManager.beginTransaction()
        val choose = TourOptionLisFragment()
        fm.add(R.id.mainFragment,choose,R.string.home.toString())
        fm.commit()
    }
}
