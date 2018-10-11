package br.com.transferr.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.Adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import br.com.transferr.R
import br.com.transferr.activities.newlayout.DriverAddPlainTourActivity
import br.com.transferr.extensions.showError
import br.com.transferr.model.PlainTour
import br.com.transferr.model.responses.OnResponseInterface
import br.com.transferr.model.responses.ResponseOK
import br.com.transferr.passenger.extensions.showAlert
import br.com.transferr.passenger.extensions.showAlertError
import br.com.transferr.passenger.util.DateUtil
import br.com.transferr.util.AddPassengerDialog
import br.com.transferr.webservices.PlainTourService
import kotlinx.android.synthetic.driver.rc_view_tours.view.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import org.jetbrains.anko.yesButton

/**
 * Created by root on 02/04/18.
 */
class TourAdapter(private val tours:List<PlainTour>,private val context: Context, val activity: Activity, val onDelete:(planTour:PlainTour)->Unit) : Adapter<TourAdapter.ViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.rc_view_tours, parent, false)
        return ViewHolder(view,context)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder?.let {
            var plainTour = tours[position]
            it.bindView(plainTour)
            it.itemView.setOnClickListener {
                startFrmPlainTourActivity(plainTour)
            }
            it.itemView.imgTrash.setOnClickListener {
                context.alert("Confirme exclusão"){
                    title = "Exclusão"
                    yesButton { onDelete(plainTour) }
                    noButton {  }
                }.show()
            }
        }
    }

    override fun getItemCount(): Int {
       return tours.size
    }

    class ViewHolder(itemView:View,val context: Context) : RecyclerView.ViewHolder(itemView){
        @SuppressLint("ResourceAsColor")
        fun bindView(tour: PlainTour) {
            val titleView       = itemView.note_item_title
            val description     = itemView.note_item_description
            titleView.text      = DateUtil.getDateDescription(tour.date!!,true)
            description.text    = tour.tourOption?.name
            /*
            itemView.imgTrash.setOnClickListener {

                context.alert("Confirme exclusão"){
                    title = "Exclusão"
                    yesButton { excluir(tour) }
                    noButton {  }
                }.show()
            }
            */
        }

        /*
        fun showMessage(message:String){
            Toast.makeText(context,message,Toast.LENGTH_SHORT).show()
        }
        */
        /*
        fun excluir(tour: PlainTour){
            PlainTourService.delete(tour.id!!,
                    object: OnResponseInterface<ResponseOK> {
                        override fun onSuccess(body: ResponseOK?) {
                            showMessage("Excluido com sucesso!")

                        }

                        override fun onError(message: String) {
                            showMessage("Erro ao tentar excluir: $message")
                        }

                        override fun onFailure(t: Throwable?) {
                            showMessage("Falha grave: ${t!!.message}")
                        }

                    }
            )
        }
        */
    }

    private fun startFrmPlainTourActivity(plainTour: PlainTour){
        var intent = Intent(context, DriverAddPlainTourActivity::class.java)
        intent.putExtra(PlainTour.PARAMETER_PLAN_TUOR,plainTour)
        startActivity(activity, intent,null)
    }



}


