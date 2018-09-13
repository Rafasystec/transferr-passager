package br.com.transferr.driver.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import br.com.transferr.passager.R
import br.com.transferr.passager.extensions.setupToolbar
import br.com.transferr.passager.extensions.switchFragmentToMainContentDriver
import br.com.transferr.passager.fragments.MapsFragment
import br.com.transferr.passager.fragments.TourOptionLisFragment
import kotlinx.android.synthetic.main.content_main.*
import org.jetbrains.anko.toast

class MainActivityDriver : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_driver)
        setupToolbar(R.id.toolbar,R.string.home)
        includeFragment()
        initNavigationBottomMenu()
    }

    private fun includeFragment(){
        val fm = supportFragmentManager.beginTransaction()
        val choose = MapsFragment()
        fm.add(R.id.mainFragmentDriver,choose,R.string.tour.toString())
        fm.commit()
    }

    fun setActionBarTitle(title: String) {
        supportActionBar!!.title = title
    }

    fun initNavigationBottomMenu() {
        btnNavigationClient.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menuHomeDriver -> {
                    //includeFragment(R.id.frameMainProfessionalAgenda, ProfessionalAgendaFragment())
                    switchFragmentToMainContentDriver(MapsFragment())

                    true
                }
                R.id.menuInfoDriver -> {
                    //includeFragment(R.id.frameMainProfessionalAgenda, ProfessionalAgendaFragment())
                    switchFragmentToMainContentDriver(TourOptionLisFragment())
                    true
                }
                R.id.menuTourDriver -> {
                    //includeFragment(R.id.frameMainProfessionalAgenda, ProfessionalHistoryFragment())
                    //switchFragment(ClientHistoryFragment())
                    toast("Angeda de passeio em desenvolvimento.")
                    true
                }
                R.id.menuDriver -> {
                    //includeFragment(R.id.frameMainProfessionalAgenda, ProfessionalHistoryFragment())
                    //switchFragment(ClientHistoryFragment())
                    toast("Menu em desenvolvimento.")
                    true
                }
                else -> {
                    true
                }
            }

        }
    }
}
