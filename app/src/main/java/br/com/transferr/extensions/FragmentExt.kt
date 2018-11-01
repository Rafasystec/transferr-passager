package br.com.transferr.extensions

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.ProgressDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Handler
import android.support.annotation.IdRes
import android.support.annotation.StringRes
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import android.support.v4.content.ContextCompat
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.TextView
import android.widget.Toast
import br.com.transferr.R
import br.com.transferr.dialogs.ProgressLoadDialog
import br.com.transferr.dialogs.indeterminateLoadingProgressDialog
import kotlinx.android.synthetic.driver.activity_main.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.indeterminateProgressDialog
import org.jetbrains.anko.okButton
import org.jetbrains.anko.yesButton
import java.util.*


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

     /*
     val dialog = activity?.indeterminateProgressDialog(message, title) {
        setProgressStyle(ProgressDialog.STYLE_SPINNER)
        setCancelable(false)
        ProgressLoadDialog(activity)
        setOnDismissListener({
             Handler().postDelayed({
                 dismiss()
             },3000)
        })
        show()
    }
    return dialog!!
    */
     return showLoadingDialogWithDelay(message,title)
}

@SuppressLint("NewApi")
fun Fragment.showLoadingDialogWithDelay(message: String = getString(R.string.loading), title: String=getString(R.string.wait)): ProgressDialog {
    val dialog = activity?.indeterminateLoadingProgressDialog(message, title) {
        setProgressStyle(ProgressDialog.STYLE_SPINNER)
        setCancelable(false)
        ProgressLoadDialog(activity)
        show()
    }
    return dialog!!
}

fun Fragment.showTimePicker(onResult: (resultTime:String) -> Unit,date:Date?=null){
    var mcurrentTime: Calendar?
    var hour         = 0
    var minute       = 0
    var mTimePicker: TimePickerDialog
    if(date != null){
        mcurrentTime = Calendar.getInstance()
        mcurrentTime.time = date
    }else{
        mcurrentTime = Calendar.getInstance()
    }
    hour         = mcurrentTime.get(Calendar.HOUR_OF_DAY)
    minute       = mcurrentTime.get(Calendar.MINUTE)
    mTimePicker = TimePickerDialog(activity, android.R.style.Theme_Holo_Dialog, TimePickerDialog.OnTimeSetListener{
        timePicker,
        selectedHour,
        selectedMinute -> onResult(if(selectedHour < 10){"0$selectedHour"}else{selectedHour.toString()}
            + ":" +
            if(selectedMinute < 10){"0$selectedMinute"}else{selectedMinute.toString()}) },
            hour,
            minute, true)//Yes 24 hour time
    mTimePicker.setTitle(activity?.getString(R.string.selectADate))
    mTimePicker.show()
}


fun Fragment.showDatePicker(onResult: (resultTime:String) -> Unit,date:Date?=null){
    var mcurrentDate = Calendar.getInstance()
    var mDatePicker: DatePickerDialog
    if(date != null){
        mcurrentDate = Calendar.getInstance()
        mcurrentDate.time = date
    }
    var mYear = mcurrentDate.get(Calendar.YEAR)
    var mMonth = mcurrentDate.get(Calendar.MONTH)
    var mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH)
    mDatePicker = DatePickerDialog(activity, OnDateSetListener { datepicker, selectedyear, selectedmonth, selectedday ->
        var selectedmonth = selectedmonth

        selectedmonth += 1
        onResult( "$selectedday/$selectedmonth/$selectedyear")
    }, mYear, mMonth, mDay)
    mDatePicker.setTitle(activity?.getString(R.string.selectADate))
    mDatePicker.show()
}

fun Fragment.showAlertValidation(message:String){
    activity?.alert(message,activity?.getString(R.string.msgValidationTitle)!!){
        okButton{ it.dismiss() }
    }?.show()
}

fun Fragment.showAlertValidation(@StringRes idResource: Int){
    showAlertValidation(activity?.getString(idResource)!!)
}

fun Fragment.showAlert(message:String){
    activity?.alert(message,activity?.getString(R.string.Advice)!!){
        okButton{ it.dismiss() }
    }?.show()
}

fun Fragment.showAlert(@StringRes idResource: Int){
    showAlert(activity?.getString(idResource)!!)
}

fun Fragment.showAlertError(message:String){
    activity?.alert(message,activity?.getString(R.string.Advice)!!){
        okButton{ it.dismiss() }
    }?.show()
}

fun Fragment.showAlertError(@StringRes idResource: Int){
    showAlertError(activity?.getString(idResource)!!)
}

fun Fragment.showAlertFailure(message:String){
    activity?.alert(message,activity?.getString(R.string.Advice)!!){
        okButton{ it.dismiss() }
    }?.show()
}

fun Fragment.showAlertFailure(@StringRes idResource: Int){
    showAlertFailure(activity?.getString(idResource)!!)
}

fun Fragment.showAlert(@StringRes idResource: Int,onOkClick : ()->Unit){
    activity?.alert(activity?.getString(idResource)!!,activity?.getString(R.string.Advice)!!){
        okButton{ onOkClick }
    }?.show()
}

fun Fragment.callEmailHost(emailTo:String, subject:String, mailBody:String, title: String){
    val intent = Intent(Intent.ACTION_SEND)
    intent.type = "plain/text"
    intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(emailTo))
    intent.putExtra(Intent.EXTRA_SUBJECT, subject)
    intent.putExtra(Intent.EXTRA_TEXT, mailBody)
    activity?.startActivity(Intent.createChooser(intent, title))
}