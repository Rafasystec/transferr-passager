package br.com.transferr.passager.adapter

import android.content.Context
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import br.com.transferr.passager.R
import br.com.transferr.passager.model.responses.ResponseDrivers
import com.squareup.picasso.Picasso

/**
 * Created by root on 25/07/18.
 */
class DriversResponseAdapter(val drivers : List<ResponseDrivers>,val onClick: (ResponseDrivers) -> Unit)
    :RecyclerView.Adapter<DriversResponseAdapter.DriversResponseViewHolder>()
{
    var context: Context?=null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DriversResponseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_drivers_response,parent,false)
        return DriversResponseViewHolder(view)
    }

    override fun getItemCount() = this.drivers.size

    override fun onBindViewHolder(holder: DriversResponseViewHolder, position: Int) {
        this.context = holder!!.itemView.context
        val responseDrivers = drivers[position]
        holder.tvName.text = responseDrivers.name
        holder.tvDriverDetail.text = "Placa: ${responseDrivers.countryRegister} - ${responseDrivers.email}"
        if(responseDrivers.imgProfileUrl?.isEmpty()!!){
            responseDrivers.imgProfileUrl = null
        }
        //Start progressBar
        //holder.progress.visibility = View.Visible
        Picasso.with(context).load(responseDrivers.imgProfileUrl).placeholder(R.drawable.no_photo_64).fit().into(holder.img,
            object : com.squareup.picasso.Callback{
                override fun onSuccess() {
                    //Stop progress bar
                }

                override fun onError() {
                    //Stop progress bar
                }

            })

    }

    class DriversResponseViewHolder(view: View):RecyclerView.ViewHolder(view){
        var tvName    : TextView
        var tvPrice   : TextView
        var img: ImageView
        var ivStarRate1: ImageView
        var ivStarRate2: ImageView
        var ivStarRate3: ImageView
        var ivStarRate4: ImageView
        var ivStarRate5: ImageView
        var tvDriverDetail : TextView
        var cardView: CardView
        init {
            tvName      = view.findViewById(R.id.tvNameDriver)
            img         = view.findViewById(R.id.ivProfile)
            tvPrice     = view.findViewById(R.id.tvPrice)
            tvDriverDetail = view.findViewById(R.id.tvDriverDetail)
            cardView    = view.findViewById(R.id.cvProfessional)
            ivStarRate1    = view.findViewById(R.id.ivStarRate1)
            ivStarRate2    = view.findViewById(R.id.ivStarRate2)
            ivStarRate3    = view.findViewById(R.id.ivStarRate3)
            ivStarRate4    = view.findViewById(R.id.ivStarRate4)
            ivStarRate5    = view.findViewById(R.id.ivStarRate5)
        }
    }
}