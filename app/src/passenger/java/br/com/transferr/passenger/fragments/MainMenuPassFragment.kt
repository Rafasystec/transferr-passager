package br.com.transferr.passenger.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.transferr.R
import br.com.transferr.activities.TermsOfUseActivity
import br.com.transferr.extensions.callEmailHost
import br.com.transferr.extensions.setupToolbar
import br.com.transferr.fragments.SuperClassFragment
import kotlinx.android.synthetic.passenger.fragment_pass_main_menu.*
import android.widget.Toast
import android.content.ActivityNotFoundException
import android.net.Uri
import br.com.transferr.extensions.toast
import hotchemi.android.rate.AppRate
import br.com.transferr.passenger.activities.MainActivity
import hotchemi.android.rate.OnClickButtonListener




/**
 * A simple [Fragment] subclass.
 */
class MainMenuPassFragment : SuperClassFragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_pass_main_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar(R.id.toolbar,getString(R.string.menu))

        activeFrameWorkToMonitorRateApp()

        btnMainMenuContact.setOnClickListener {
            callEmailHost("contact.boora@gmail.com",getString(R.string.emailSubject),getString(R.string.emailBody),getString(R.string.emailTitle))
        }

        btnMainMenuTerms.setOnClickListener {
            activity?.startActivity(Intent(activity, TermsOfUseActivity::class.java))
        }
        btnMainMenuRateApp.setOnClickListener {
            //rateTheAppOnStore()
            AppRate.with(activity).showRateDialog(activity)
        }

    }

/*    private fun logout(){
        activity?.alert(getString(R.string.logout),getString(R.string.areYouSureLogout)){
            yesButton {
                Prefes.clear()
                startActivity(Intent(activity, LoginActivity::class.java))
                activity!!.finish()
            }
            noButton {
                it.dismiss()
            }
        }?.show()

    }
*/
    private fun rateTheAppOnStore() {
        val uri = Uri.parse("market://details?id=br.com.idoctorbrasil.idoctor_android")// + activity!!.packageName)
        val myAppLinkToMarket = Intent(Intent.ACTION_VIEW, uri)
        try {
            startActivity(myAppLinkToMarket)
        } catch (e: ActivityNotFoundException) {
            toast( getString(R.string.unableToFindApp ) )
        }

    }

    private fun activeFrameWorkToMonitorRateApp(){
        AppRate.with(activity)
                .setInstallDays(0) // default 10, 0 means install day.
                .setLaunchTimes(3) // default 10
                .setRemindInterval(2) // default 1
                .setShowLaterButton(true) // default true
                .setDebug(false) // default false
                //.setTextRateNow(R.string.appRateText)
                .setOnClickButtonListener { which ->
                    // callback listener.
                    //Log.d(MainActivity::class.java.name, Integer.toString(which))
                }
                .monitor()

        // Show a dialog if meets conditions
        AppRate.showRateDialogIfMeetsConditions(activity)
    }


}// Required empty public constructor