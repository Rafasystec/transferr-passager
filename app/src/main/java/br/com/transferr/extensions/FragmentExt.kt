package br.com.transferr.extensions

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.app.ProgressDialog.show
import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import android.support.v4.content.ContextCompat.getDrawable
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.widget.Toast
import br.com.transferr.R
import kotlinx.android.synthetic.passenger.progress_bar_circle_layout.*
import org.jetbrains.anko.Android
import org.jetbrains.anko.indeterminateProgressDialog

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
            ?.addToBackStack(fragment.javaClass.name)
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

fun Fragment.backToFragment(fragment: Fragment){
    fragmentManager?.popBackStackImmediate(fragment.javaClass.name, FragmentManager.POP_BACK_STACK_INCLUSIVE)
}

 @SuppressLint("NewApi")
 fun Fragment.showLoadingDialog(message: String = getString(R.string.loading), title: String=getString(R.string.wait)): ProgressDialog {
    val dialog = activity?.indeterminateProgressDialog(message, title) {
        //val drawable = activity!!.getDrawable(android.R.drawable.progress_indeterminate_horizontal)
        //setProgressDrawable(drawable)
        setProgressStyle(ProgressDialog.STYLE_SPINNER)
        show()
    }
    return dialog!!
}


