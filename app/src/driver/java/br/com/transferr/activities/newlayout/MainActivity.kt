package br.com.transferr.activities.newlayout

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import br.com.transferr.R
import br.com.transferr.extensions.setupToolbar
import br.com.transferr.fragments.DriverEditInfo
import br.com.transferr.fragments.DriverShowInfoFragment
import br.com.transferr.fragments.MapsFragment
import br.com.transferr.passenger.extensions.switchFragmentToMainContent
import kotlinx.android.synthetic.driver.content_main.*


import org.jetbrains.anko.toast

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_new)
        setupToolbar(R.id.toolbar,getString(R.string.home))
        includeFragment()
        initNavigationBottomMenu()
    }

    fun setActionBarTitle(title: String) {
        supportActionBar!!.title = title
    }

    private fun includeFragment(){
        val fm = supportFragmentManager.beginTransaction()
        val choose = MapsFragment()//MapsFragment()
        fm.add(R.id.mainFragment,choose,R.string.tour.toString())
        fm.commit()
    }

    fun initNavigationBottomMenu() {
        btnNavigationDriver.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menuHomeDriver-> {
                    switchFragmentToMainContent(MapsFragment())
                    true
                }
                R.id.menuInfoDriver -> {
                    switchFragmentToMainContent(DriverShowInfoFragment())
                    true
                }
                R.id.menuDriver -> {
                    toast("Ainda em desenvolvimento.")
                    true
                }
                else -> {
                    true
                }
            }

        }
    }
}
