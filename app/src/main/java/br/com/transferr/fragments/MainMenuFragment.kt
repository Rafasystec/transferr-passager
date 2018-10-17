package br.com.transferr.fragments


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.transferr.R
import br.com.transferr.activities.TermsOfUseActivity
import br.com.transferr.extensions.callEmailHost
import br.com.transferr.extensions.setupToolbar
import kotlinx.android.synthetic.main.fragment_main_menu.*


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
            callEmailHost("transferr.app@gmail.com",getString(R.string.emailSubject),getString(R.string.emailBody),getString(R.string.emailTitle))
        }

        btnMainMenuTerms.setOnClickListener {
            activity?.startActivity(Intent(activity,TermsOfUseActivity::class.java))
        }
    }

}// Required empty public constructor
