package br.com.transferr.passager.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import br.com.transferr.passager.R
import br.com.transferr.passager.extensions.setupToolbar
import br.com.transferr.passager.extensions.switchFragmentToMainContent
import br.com.transferr.passager.fragments.MapsFragment
import br.com.transferr.passager.fragments.TourOptionLisFragment
import kotlinx.android.synthetic.main.content_main.*
import org.jetbrains.anko.toast

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupToolbar(R.id.toolbar,R.string.home)
        includeFragment()
        initNavigationBottomMenu()
    }


    private fun includeFragment(){
        val fm = supportFragmentManager.beginTransaction()
        val choose = MapsFragment()
        fm.add(R.id.mainFragment,choose,R.string.home.toString())
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
