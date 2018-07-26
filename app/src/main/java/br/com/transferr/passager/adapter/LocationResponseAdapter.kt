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
import br.com.transferr.passager.model.responses.ResponseLocation
import com.squareup.picasso.Picasso

/**
 * Created by root on 25/07/18.
 */
class LocationResponseAdapter(val locations : List<ResponseLocation>, val onClick: (ResponseLocation) -> Unit)
    : RecyclerView.Adapter<LocationResponseAdapter.LocationsResponseViewHolder>()
{
    var context: Context?=null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationsResponseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_location_response,parent,false)
        return LocationsResponseViewHolder(view)
    }

    override fun getItemCount() = this.locations.size

    override fun onBindViewHolder(holder: LocationsResponseViewHolder, position: Int) {
        this.context = holder!!.itemView.context
        val responseDrivers = locations[position]
        holder.tvName.text = responseDrivers.name
        holder.cardView.setOnClickListener { this }
    }

    class LocationsResponseViewHolder(view: View): RecyclerView.ViewHolder(view){
        var tvName    : TextView
        var cardView : CardView
        init {
            tvName      = view.findViewById(R.id.tvLocationName)
            cardView    = view.findViewById(R.id.cvLocation)
        }
    }
}