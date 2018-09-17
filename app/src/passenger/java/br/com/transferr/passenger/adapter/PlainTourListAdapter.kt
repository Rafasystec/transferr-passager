package br.com.transferr.passenger.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v7.widget.AppCompatButton
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import br.com.transferr.R
import br.com.transferr.passenger.model.PlainTour
import br.com.transferr.passenger.util.DateUtil
import br.com.transferr.passenger.util.WhatsAppUtil
import com.squareup.picasso.Picasso

/**
 * Created by Rafael Rocha on 10/08/2018.
 */
class PlainTourListAdapter(val plainsTour: List<PlainTour> ,val onClick: (PlainTour)->Unit)
    :RecyclerView.Adapter<br.com.transferr.passenger.adapter.PlainTourListAdapter.PlainTourViewHolder>()
{
    var context:Context?=null
    override fun getItemCount():Int = plainsTour.size

    @SuppressLint("SetTextI18n", "MissingPermission")
    override fun onBindViewHolder(holder: br.com.transferr.passenger.adapter.PlainTourListAdapter.PlainTourViewHolder, position: Int) {
        this.context = holder!!.itemView.context
        val plain = plainsTour[position]
        holder.tvName.text = plain.driver?.name
        //holder.tvDriverDetail.text = "${this.context!!.getString(R.string.seatsRemaining)} ${plain.seatsRemaining}"
        holder.tvSeatsRemindingNumber.text  = ""+plain.seatsRemaining
        holder.tvDriverDetail.text          = plain.driver?.car?.model
        var photoUrl = if(plain.driver?.car?.photo != null && plain.driver?.car?.photo!!.isEmpty()!!){
            null
        }else{
             plain.driver?.car?.photo
        }
        holder.tvDriverCarPlate.text = plain.driver?.car?.carIdentity
        holder.tvPlainDate.text = DateUtil.format(plain.date!!,"dd/MM/yyyy HH:mm")
        holder.tvPliantourName.text = plain.tourOption?.name

        //Start progressBar
        //holder.progress.visibility = View.Visible
        Picasso.with(context).load(photoUrl).placeholder(R.drawable.no_photo_64).fit().into(holder.img,
                object : com.squareup.picasso.Callback{
                    override fun onSuccess() {
                        //Stop progress bar
                    }

                    override fun onError() {
                        //Stop progress bar
                    }

                })
        holder.cardView.setOnClickListener { onClick(plain) }
        holder.btnWhatsapp.setOnClickListener {
            WhatsAppUtil.callWhatsapp(""+plain.driver?.whatsapp,context!!)
        }

        holder.btnCallPhone.setOnClickListener {
            var phoneNumber = "${plain.driver?.ddd}${plain.driver?.phone}"
            context!!.startActivity(Intent(Intent.ACTION_CALL, Uri.parse("tel:$phoneNumber")))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): br.com.transferr.passenger.adapter.PlainTourListAdapter.PlainTourViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_plain_tour_list,parent,false)
        return br.com.transferr.passenger.adapter.PlainTourListAdapter.PlainTourViewHolder(view)
    }


    class PlainTourViewHolder(view: View) : RecyclerView.ViewHolder(view){
        var tvName          : TextView  = view.findViewById(R.id.tvNameDriver)
        var tvPliantourName : TextView  = view.findViewById(R.id.tvPlainTourName)
        var img             : ImageView = view.findViewById(R.id.ivProfile)
        var tvDriverDetail  : TextView  = view.findViewById(R.id.tvDriverDetail)
        var tvDriverCarPlate: TextView  = view.findViewById(R.id.tvDriverCarPlate)
        var cardView        : CardView  = view.findViewById(R.id.cvProfessional)
        var btnCallPhone    : AppCompatButton = view.findViewById(R.id.btnCallPhone)
        var btnWhatsapp     : AppCompatButton = view.findViewById(R.id.btnWhatsapp)
        var tvSeatsRemindingNumber : TextView = view.findViewById(R.id.tvSeatsRemindingNumber)
        var tvPlainDate : TextView = view.findViewById(R.id.tvPlainDate)
    }
}