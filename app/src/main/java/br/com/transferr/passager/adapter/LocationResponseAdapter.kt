package br.com.transferr.passager.adapter

import android.content.Context
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import br.com.transferr.passager.R
import br.com.transferr.passager.model.responses.ResponseDrivers
import br.com.transferr.passager.model.responses.ResponseLocation
import com.squareup.picasso.MemoryPolicy
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
        var urlPhoto: String
        urlPhoto = responseDrivers.urlMainPicture;
        holder.progress.visibility = View.VISIBLE
        Picasso.with(context).load(urlPhoto).fit().memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE).into(holder.ivMainLocation,
                object : com.squareup.picasso.Callback {
                    override fun onSuccess() {
                        holder.progress.visibility = View.GONE
                    }

                    override fun onError() {
                        holder.progress.visibility = View.GONE
                    }
                })
        holder.cardView.setOnClickListener { onClick(responseDrivers) }

    }

    class LocationsResponseViewHolder(view: View): RecyclerView.ViewHolder(view){
        var tvName          : TextView
        var cardView        : CardView
        var ivMainLocation  : ImageView
        var progress        : ProgressBar
        init {
            tvName      = view.findViewById(R.id.tvLocationName)
            cardView    = view.findViewById(R.id.cvLocation)
            ivMainLocation = view.findViewById(R.id.ivMainLocation)
            progress    = view.findViewById(R.id.progress)
        }
    }
}