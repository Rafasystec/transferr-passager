package br.com.transferr.fragments

import android.app.ProgressDialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import br.com.transferr.passager.R
import br.com.transferr.passager.fragments.TourOptionLisFragment
import org.jetbrains.anko.progressDialog

/**
 * Created by Rafael Rocha on 06/02/18.
 */

open class SuperClassFragment : Fragment(){
    //protected var dialog: ProgressDialog? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //dialog = activity?.progressDialog(message = R.string.loading, title = R.string.wait)
        super.onViewCreated(view, savedInstanceState)
    }

    protected fun includeFragmentOnMainActivity(fragment: SuperClassFragment){
        if(activity != null) {
            val fm = activity?.supportFragmentManager?.beginTransaction()
            fm?.add(R.id.mainFragment, fragment, R.string.home.toString())
            fm?.commit()
        }
    }
}