package br.com.transferr.activities.newlayout


import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import br.com.transferr.R
import br.com.transferr.extensions.setupToolbar
import br.com.transferr.extensions.toast
import br.com.transferr.fragments.DriverListPlainTourFragment
import br.com.transferr.fragments.DriverShowInfoFragment
import br.com.transferr.fragments.MapsFragment
import br.com.transferr.main.util.Prefes
import br.com.transferr.model.Driver
import br.com.transferr.model.responses.OnResponseInterface
import br.com.transferr.passenger.extensions.showLoadingDialog
import br.com.transferr.passenger.extensions.switchFragmentToMainContent
import br.com.transferr.webservices.DriverService
import kotlinx.android.synthetic.driver.content_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_new)
        setupToolbar(R.id.toolbar,getString(R.string.home))
        includeFragment()
        initNavigationBottomMenu()
        getAndSaveDriver()
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
                R.id.menuTourDriver ->{
                    switchFragmentToMainContent(DriverListPlainTourFragment())
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

    fun getAndSaveDriver(){
        var driver = Prefes.driver
        if(driver == null || driver.id == 0L){
            getDriverFromWebService()
        }
    }
    private fun getDriverFromWebService(){
        var progress = showLoadingDialog(message = getString(R.string.getTheDriver))
        DriverService.doGetByUserId(Prefes.prefsLogin,
                object: OnResponseInterface<Driver> {
                    override fun onSuccess(driver: Driver?) {
                        progress.dismiss()
                        Prefes.driver = driver!!
                    }

                    override fun onError(message: String) {
                        progress.dismiss()

                    }

                    override fun onFailure(t: Throwable?) {
                        progress.dismiss()

                    }

                }
        )

    }
}
