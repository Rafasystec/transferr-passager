package br.com.transferr.extensions


import android.app.Activity
import android.app.AlertDialog
import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.TextView
import android.widget.Toast
import br.com.transferr.R

/**
 * Created by root on 06/02/18.
 */

fun Activity.toast(message: CharSequence, lenth: Int = Toast.LENGTH_SHORT) =
        Toast.makeText(this,message,lenth).show()
fun Activity.getTextAsString(@IdRes id:Int):String{
    val textView = findViewById<TextView>(id)
    return textView.text.toString()
}
fun Activity.onClick(@IdRes viewId: Int, onClick:(v: android.view.View?) -> Unit){
    val view = findViewById<View>(viewId)
    view.setOnClickListener{onClick(it)}
}

fun AppCompatActivity.setupToolbar(@IdRes id: Int, title: String? = null, upNavigation : Boolean = false): ActionBar {
    val toolbar = findViewById<Toolbar>(id)
    setSupportActionBar(toolbar)
    if(title != null){
        supportActionBar?.title = title
    }
    supportActionBar?.setDisplayHomeAsUpEnabled(upNavigation)
    return supportActionBar!!
}

fun AppCompatActivity.addFragment(@IdRes layoutId:Int, fragment: Fragment){
    fragment.arguments = intent.extras
    val ft = supportFragmentManager.beginTransaction()
    ft.add(layoutId,fragment)
    ft.commit()
}

fun Activity.showError(message: CharSequence) {
    var messageError = "Erro desconhecido."
    if(message == null){
        messageError = message
    }
    Toast.makeText(this, messageError, Toast.LENGTH_SHORT).show()
}

fun Activity.showValidation(message: CharSequence) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Activity.showError(throwable: Throwable?) {
    var message = "Erro desconecido"
    if(throwable?.message != null){
        message = throwable.message!!
    }
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}
/*
fun Activity.showDialog(){
    val dialog = AlertDialog.Builder(this)
    val dialogView = layoutInflater.inflate (R.layout.dialog_add_plaintour,null)
}
        */