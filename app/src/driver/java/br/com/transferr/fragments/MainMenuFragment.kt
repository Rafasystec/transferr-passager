package br.com.transferr.fragments


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.transferr.BuildConfig
import br.com.transferr.R
import br.com.transferr.activities.LoginActivity
import br.com.transferr.activities.TermsOfUseActivity
import br.com.transferr.extensions.callEmailHost
import br.com.transferr.extensions.setupToolbar
import br.com.transferr.main.util.Prefes
import kotlinx.android.synthetic.main.fragment_main_menu.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import org.jetbrains.anko.yesButton


/**
 * A simple [Fragment] subclass.
 */
class MainMenuFragment : SuperClassFragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar(R.id.toolbar,getString(R.string.menu))

        btnMainMenuContact.setOnClickListener {
            callEmailHost("contact.boora@gmail.com",getString(R.string.emailSubject),getString(R.string.emailBody),getString(R.string.emailTitle))
        }

        btnMainMenuTerms.setOnClickListener {
            activity?.startActivity(Intent(activity,TermsOfUseActivity::class.java))
        }

        if(BuildConfig.FLAVOR == "driver"){
            btnLogout.visibility = View.VISIBLE
            btnLogout.setOnClickListener { logout() }
        }else{
            btnLogout.visibility = View.GONE
        }
    }

    private fun logout(){
        activity?.alert(getString(R.string.logout),getString(R.string.areYouSureLogout)){
            yesButton {
                Prefes.clear()
                startActivity(Intent(activity,LoginActivity::class.java))
                activity!!.finish()
            }
            noButton {
                it.dismiss()
            }
        }?.show()

    }



}// Required empty public constructor
