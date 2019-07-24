package br.com.transferr.passenger.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import br.com.transferr.R
import br.com.transferr.dialogs.CustomDIalogSendEmail
import br.com.transferr.main.util.PicassoUtil
import br.com.transferr.passenger.extensions.setupToolbar
import br.com.transferr.passenger.extensions.switchFragmentToMainContent
import br.com.transferr.passenger.fragments.MainMenuPassFragment
import br.com.transferr.passenger.fragments.MapsFragment
import br.com.transferr.passenger.fragments.TourOptionLisFragment
import br.com.transferr.passenger.model.Location
import com.squareup.picasso.LruCache
import kotlinx.android.synthetic.passenger.content_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupToolbar(R.id.toolbar,R.string.home)
        includeFragment()
        initNavigationBottomMenu()
        PicassoUtil.buildGlobalCache()

    }

    fun setActionBarTitle(title: String) {
        supportActionBar!!.title = title
    }

    private fun includeFragment(){
        val fm = supportFragmentManager.beginTransaction()
        //val choose = TourOptionLisFragment()//MapsFragment()
        var location = intent.getSerializableExtra(Location.LOCATION)
        var fragment = TourOptionLisFragment()
        if(location != null){
            fragment.arguments = Bundle()
            fragment.arguments!!.putSerializable(Location.LOCATION,location)
        }
        fm.add(R.id.mainFragment,fragment,R.string.tour.toString())
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

                    switchFragmentToMainContent(TourOptionLisFragment())
                    true
                }
                R.id.menuHistoryCli -> {
                    switchFragmentToMainContent(MainMenuPassFragment())
                    true
                }
                else -> {
                    true
                }
            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("DESTROY","On destroy called.")
        PicassoUtil.clearGlobalCache()
    }


}
