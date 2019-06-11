package br.com.transferr.activities.newlayout


import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import br.com.transferr.R
import br.com.transferr.activities.LoginActivity
import br.com.transferr.extensions.setupToolbar
import br.com.transferr.extensions.toast
import br.com.transferr.fragments.DriverListPlainTourFragment
import br.com.transferr.fragments.DriverShowInfoFragment
import br.com.transferr.fragments.MainMenuFragment
import br.com.transferr.fragments.MapsFragment
import br.com.transferr.main.util.Prefes
import br.com.transferr.model.Driver
import br.com.transferr.model.responses.OnResponseInterface
import br.com.transferr.passenger.extensions.setupToolbar
import br.com.transferr.passenger.extensions.showLoadingDialog
import br.com.transferr.passenger.extensions.switchFragmentToMainContent
import br.com.transferr.util.NetworkUtil
import br.com.transferr.webservices.DriverService
import kotlinx.android.synthetic.driver.content_main.*
import org.jetbrains.anko.toast

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_new)
        setupToolbar(R.id.toolbar,getString(R.string.home))
        includeFragment()
        initNavigationBottomMenu()
        //getAndSaveDriver()
        //checkLogin()
    }
    /*
    fun setActionBarTitle(title: String) {
        supportActionBar!!.title = title
    }
    */

    override fun onResume() {
        super.onResume()
        checkLogin()
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
                   switchFragmentToMainContent(MainMenuFragment())
                    true
                }
                else -> {
                    true
                }
            }

        }
    }
/*
    fun getAndSaveDriver(){
        var driver = Prefes.driver
        if(driver == null || driver.id == 0L){
            getDriverFromWebService()
        }
    }
    */
    private fun getDriverFromWebService(){
        val progress = showLoadingDialog(message = getString(R.string.getTheDriver))
        DriverService.doGetByUserId(Prefes.prefsLogin,
                    object: OnResponseInterface<Driver> {
                        override fun onSuccess(driver: Driver?) {
                            progress.dismiss()
                            Prefes.driver   = driver!!
                            Prefes.prefsCar = driver!!.car?.id!!
                        }
                    }
            ,this,progress)


    }

    private fun checkLogin(){
        val isLoged = checkUserLogin()
        if(isLoged) {
            if(isConnected()) {
                getDriverFromWebService()
            }else{
                // showError("Sem conexão com a Internet.")
            }

        }else{
            callLoginActivity()
        }
    }

    private fun callLoginActivity(){
        startActivity(Intent(this, LoginActivity::class.java))

    }

    private fun isConnected():Boolean{
        return NetworkUtil.isNetworkAvailable(this)
    }

    private fun checkUserLogin():Boolean{
        val id = Prefes.prefsLogin
        if(id == null || id <= 0){
            return false
        }
        return true
    }
    /*
    private fun getCarFromWebService(){
        var progress = showLoadingDialog(message = getString(R.string.pleaseWaitWhileWeGetTheCar))
        DriverService.doGetByUserId(Prefes.prefsLogin,
                object: OnResponseInterface<Driver> {
                    override fun onSuccess(body: Driver?) {
                        Prefes.driver = body!!
                        progress.dismiss()
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
    */
}
