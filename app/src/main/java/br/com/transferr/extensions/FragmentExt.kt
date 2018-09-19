package br.com.transferr.extensions

import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.widget.Toast
import br.com.transferr.R

/**
 * Created by Rafael Rocha on 07/08/2018.
 */
inline fun Fragment.defaultRecycleView(view: FragmentActivity, resId:Int): RecyclerView {
    var recycleView = view.findViewById<RecyclerView>(resId)
    recycleView?.layoutManager = LinearLayoutManager(activity)
    recycleView?.itemAnimator = DefaultItemAnimator()
    recycleView?.setHasFixedSize(true)
    return recycleView
}

inline fun Fragment.setupToolbar(@IdRes id: Int, title:String?= null, upNavigation: Boolean = false) : ActionBar {

    val activityCompat = activity as AppCompatActivity
    val toolbar = activity?.findViewById<Toolbar>(id)
    activityCompat.setSupportActionBar(toolbar)
    if(title != null){
        activityCompat.supportActionBar?.title = title
    }
    activityCompat.supportActionBar?.setHomeButtonEnabled(true)
    return activityCompat.supportActionBar!!
}

fun Fragment.switchFragmentToMainContent(fragment: Fragment){
    fragmentManager?.beginTransaction()
            ?.replace(R.id.mainFragment,fragment)
            ?.commit()

}

fun Fragment.toast(message: CharSequence, length: Int = Toast.LENGTH_SHORT) =
        Toast.makeText(context,message,length).show()

fun Fragment.showValidation(message: CharSequence) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun Fragment.showError(throwable: Throwable?) {
    var message = "Erro desconecido"
    if(throwable?.message != null){
        message = throwable.message!!
    }
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}


