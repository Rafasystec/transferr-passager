package br.com.transferr.fragments

import android.os.Bundle
import android.support.annotation.StringRes
import android.support.v4.app.Fragment
import android.view.View
import android.widget.ProgressBar
import br.com.transferr.R
import br.com.transferr.passenger.activities.MainActivity
import br.com.transferr.passenger.util.AlertUtil
import org.jetbrains.anko.alert
import org.jetbrains.anko.okButton

/**
 * Created by Rafael Rocha on 06/02/18.
 */

open class SuperClassFragment : Fragment(){
    var progress: ProgressBar?=null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        progress = view.findViewById(R.id.ProgressBar)
        super.onViewCreated(view, savedInstanceState)
    }

    protected fun includeFragmentOnMainActivity(fragment: SuperClassFragment){
        if(activity != null) {
            val fm = activity?.supportFragmentManager?.beginTransaction()
            fm?.add(R.id.mainFragment, fragment, R.string.home.toString())
            fm?.commit()
        }
    }

    protected fun alertWarning(message: String){
        activity!!.alert ( message, AlertUtil.DEFAULT_VALIDATE_TITLE){
            okButton { it.dismiss() }
        }.show()
    }

    protected fun alertErro(message: String){
        activity!!.alert ( message, AlertUtil.DEFAULT_ERROR_TITLE){
            okButton { it.dismiss() }
        }.show()
    }

    fun setMainTitle(@StringRes resId:Int){
        if(activity != null) {
            (activity as br.com.transferr.passenger.activities.MainActivity).setActionBarTitle(getString(resId))
        }
    }

    fun startProgressBar(){
        progress?.visibility = View.VISIBLE
    }

    fun stopProgressBar(){

        Thread({
            Thread.sleep(1000)
            activity?.runOnUiThread({
                progress?.visibility = View.GONE
            })
        }).start()

    }
}