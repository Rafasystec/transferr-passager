package br.com.transferr.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import br.com.transferr.R
import br.com.transferr.broadcast.InternetBroadCast
import br.com.transferr.main.util.Prefes
import kotlinx.android.synthetic.driver.toolbar.*

/**
 * Created by root on 06/02/18.
 */
@SuppressLint("Registered")
open class SuperClassActivity : AppCompatActivity(),InternetBroadCast.ConnectivityReceiverListener {
    protected val context: Context get() = this
    private var mSnackBar: Snackbar? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("DEBUG","setSupportActionBar")
        setSupportActionBar(toolbar)
    }

    override fun onResume() {
        super.onResume()
        InternetBroadCast.connectivityReceiverListener = this
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        //return super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu_toolbar,menu)
        Log.d("DEBUG","Creating default menu for all activities")
        return true
    }



    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_exit -> {
            // User chose the "Settings" item, show the app settings UI...
            logout()
            callLoginActivity()
            true
        }

        R.id.action_mapa -> {
            // User chose the "Favorite" action, mark the current item
            startMap()
            true
        }

        R.id.action_plain_tour -> {
            // User chose the "Favorite" action, mark the current item
            startPlainTour()
            true
        }

        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }

    private fun logout(){
        Prefes.prefsLogin = 0
    }

    private fun callLoginActivity(){
        startActivity(Intent(context,LoginActivity::class.java))
    }

    private fun startMap(){
        startActivity(Intent(context,InitialActivity::class.java))
    }

    private fun startPlainTour(){
        startActivity(Intent(context,PlainTourActivity::class.java))
    }

    protected fun showMessage(isConnected: Boolean) {

        if (!isConnected) {
            val messageToUser = "Você está off-line." //TODO
            mSnackBar = Snackbar.make( findViewById(R.id.mainActivity), messageToUser, Snackbar.LENGTH_LONG) //Assume "rootLayout" as the root layout of every activity.
            mSnackBar?.duration = Snackbar.LENGTH_INDEFINITE
            mSnackBar?.show()

        } else {
            mSnackBar?.dismiss()
        }
    }

    /**
     * Callback will be called when there is change
     */
    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        showMessage(isConnected)
    }

}