package br.com.transferr.util

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import br.com.transferr.R
import br.com.transferr.extensions.showError
import br.com.transferr.extensions.showValidation
import br.com.transferr.extensions.toast
import br.com.transferr.model.PlainTour
import br.com.transferr.model.responses.OnResponseInterface
import br.com.transferr.webservices.PlainTourService
import kotlinx.android.synthetic.driver.dialog_add_plaintour.view.*

/**
 * Created by root on 04/04/18.
 */
class AddPassengerDialog(val context: Context, val activity: Activity,val plainTour: PlainTour) {

    fun create():AlertDialog{
        val dialog = AlertDialog.Builder(context)
        val dialogView = activity.layoutInflater.inflate(R.layout.dialog_add_plaintour,null)
        var totalPassenger = 0
        var alertDialog:AlertDialog?=null
        dialogView.btnSavePlain.setOnClickListener {
            //activity.toast("OK pressed id Plain is ${plainTour.id} and Total is $totalPassenger")
            PlainTourService.increaseSeats(plainTour.id!!,totalPassenger,
                    object : OnResponseInterface<PlainTour> {
                        override fun onSuccess(body: PlainTour?) {
                            activity.toast("Alterado com sucesso!!")
                            alertDialog?.dismiss()
                        }

                        override fun onError(message: String) {
                            activity.showValidation(message)
                        }

                        override fun onFailure(t: Throwable?) {
                            activity.showError(t)
                        }

                    }
            )

        }
        dialogView.btnCancelPlain.setOnClickListener {
            //activity.toast("Cancel")
            alertDialog?.dismiss()
        }

        dialogView.btnAddPassenger.setOnClickListener {
            totalPassenger++
            dialogView.lblAddPassenger.text = totalPassenger.toString()
        }

        dialogView.btnLessPassenger.setOnClickListener {
            totalPassenger--
            dialogView.lblAddPassenger.text = totalPassenger.toString()
        }
        dialog.setCancelable(true)
        dialog.setView(dialogView)
        alertDialog = dialog.create()
        return alertDialog
    }
}