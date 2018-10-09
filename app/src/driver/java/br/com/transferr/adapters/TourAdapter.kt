package br.com.transferr.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.Adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import br.com.transferr.R
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
class TourAdapter(private val tours:List<PlainTour>,private val context: Context, val activity: Activity) : Adapter<TourAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.rc_view_tours, parent, false)
        return ViewHolder(view,context)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder?.let {
            var plainTour = tours[position]
            it.bindView(plainTour)
            it.itemView.setOnClickListener {
                //Toast.makeText(context,"Ao clicar será modificado",Toast.LENGTH_SHORT).show()
                val dialog = AddPassengerDialog(context,activity,plainTour)
                var alertDialog = dialog.create()
                alertDialog.setOnDismissListener{
                    this.notifyDataSetChanged()
                }
                alertDialog.show()
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
            //val seats           = itemView.seats
            titleView.text      = DateUtil.getDateDescription(tour.date!!,true)
            description.text    = tour.tourOption?.name
            /*
            if( tour.seatsRemaining == 0){
                seats.text = "Esgotado"
                seats.setTextColor(Color.RED)
            }else {
                seats.setTextColor(R.color.primary)
                seats.text = "Vagas: " + tour.seatsRemaining
            }
            */
            itemView.imgTrash.setOnClickListener {
                //Toast.makeText(context,"Excluir item ${tour.id}",Toast.LENGTH_LONG).show()
                context.alert("Confirme exclusão"){
                    title = "Exclusão"
                    yesButton { excluir(tour) }
                    noButton {  }
                }.show()
            }
        }


        fun showMessage(message:String){
            Toast.makeText(context,message,Toast.LENGTH_SHORT).show()
        }

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
    }




}


