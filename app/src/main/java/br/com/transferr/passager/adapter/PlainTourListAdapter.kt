package br.com.transferr.passager.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import br.com.transferr.passager.R
import br.com.transferr.passager.model.PlainTour
import com.squareup.picasso.Picasso

/**
 * Created by Rafael Rocha on 10/08/2018.
 */
class PlainTourListAdapter(val plainsTour: List<PlainTour> ,val onClick: (PlainTour)->Unit)
    :RecyclerView.Adapter<PlainTourListAdapter.PlainTourViewHolder>()
{
    var context:Context?=null
    override fun getItemCount():Int = plainsTour.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: PlainTourViewHolder, position: Int) {
        this.context = holder!!.itemView.context
        val plain = plainsTour[position]
        holder.tvName.text = plain.driver?.name
        holder.tvDriverDetail.text = "${this.context!!.getString(R.string.seatsRemaining)} ${plain.seatsRemaining}"
        var photoUrl = if(plain.driver?.car?.photo != null && plain.driver?.car?.photo!!.isEmpty()!!){
            null
        }else{
            plain.driver?.car?.photo
        }
        holder.tvDriverPhone.text = ""+plain.driver?.phone
        //holder.tvDriverEmail.text = plain.driver.
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
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlainTourViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_plain_tour_list,parent,false)
        return PlainTourViewHolder(view)
    }


    class PlainTourViewHolder(view: View) : RecyclerView.ViewHolder(view){
        var tvName          : TextView  = view.findViewById(R.id.tvNameDriver)
        var tvPrice         : TextView  = view.findViewById(R.id.tvPrice)
        var img             : ImageView = view.findViewById(R.id.ivProfile)
        var tvDriverDetail  : TextView  = view.findViewById(R.id.tvDriverDetail)
        var tvDriverEmail   : TextView  = view.findViewById(R.id.tvDriverEmail)
        var cardView        : CardView  = view.findViewById(R.id.cvProfessional)
        var tvDriverPhone   : TextView  = view.findViewById(R.id.tvDriverPhone)
    }
}