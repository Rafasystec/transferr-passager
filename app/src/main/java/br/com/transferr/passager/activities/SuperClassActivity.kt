package br.com.transferr.passager.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import br.com.transferr.util.Prefes

/**
 * Created by root on 14/02/18.
 */
@SuppressLint("Registered")
class SuperClassActivity : AppCompatActivity(){
    //Propriedade para acessar o contexto de qualquer lugar
    protected val context: Context get() = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("DEBUG","setSupportActionBar")
        //setSupportActionBar(toolbar)
    }
/*
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        //return super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu_toolbar,menu)
        //toast("Criando menu")
        return true
    }

*/
/*
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_exit -> {
            // User chose the "Settings" item, show the app settings UI...
            logout()
            callLoginActivity()
            //finishAndRemoveTask()
            //System.exit(0)
            true
        }
/*
        R.id.action_favorite -> {
            // User chose the "Favorite" action, mark the current item
            // as a favorite...
            true
        }
*/
        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }

    private fun logout(){
        var prefes = Prefes(this)
        prefes.prefsLogin = 0
    }

    private fun callLoginActivity(){
        startActivity(Intent(context,LoginActivity::class.java))
    }
*/

}