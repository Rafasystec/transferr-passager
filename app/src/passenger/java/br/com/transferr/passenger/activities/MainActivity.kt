package br.com.transferr.passenger.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import br.com.transferr.R
import br.com.transferr.passenger.extensions.setupToolbar
import br.com.transferr.passenger.extensions.switchFragmentToMainContent
import br.com.transferr.passenger.fragments.MapsFragment
import br.com.transferr.passenger.fragments.TourOptionLisFragment
import kotlinx.android.synthetic.passenger.content_main.*
import org.jetbrains.anko.toast

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupToolbar(R.id.toolbar,R.string.home)
        includeFragment()
        initNavigationBottomMenu()
    }

    fun setActionBarTitle(title: String) {
        supportActionBar!!.title = title
    }

    private fun includeFragment(){
        val fm = supportFragmentManager.beginTransaction()
        val choose = TourOptionLisFragment()//MapsFragment()
        fm.add(R.id.mainFragment,choose,R.string.tour.toString())
        fm.commit()
    }

    fun initNavigationBottomMenu() {
        btnNavigationClient.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menuHomeCli -> {
                    //includeFragment(R.id.frameMainProfessionalAgenda, ProfessionalAgendaFragment())
                    switchFragmentToMainContent(MapsFragment())

                    true
                }
                R.id.menuAgendaCli -> {
                    //includeFragment(R.id.frameMainProfessionalAgenda, ProfessionalAgendaFragment())
                    switchFragmentToMainContent(TourOptionLisFragment())
                    true
                }
                R.id.menuHistoryCli -> {
                    //includeFragment(R.id.frameMainProfessionalAgenda, ProfessionalHistoryFragment())
                    //switchFragment(ClientHistoryFragment())
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
