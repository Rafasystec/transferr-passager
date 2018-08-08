package br.com.transferr.passager.adapter

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import br.com.transferr.passager.R
import br.com.transferr.passager.model.Location
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso

/**
 * Created by idoctor on 08/08/2018.
 */
class LocationAdapter(val locations: List<Location>, val onClick:(Location)->Unit) : RecyclerView.Adapter<LocationAdapter.LocationViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_location_response,parent,false)
        return LocationViewHolder(view)
    }

    var locationListFiltered:List<Location>?=null
    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        val location = locations[position]
        holder.tvName.text = location.name
        var urlPhoto: String
        urlPhoto = location.photoProfile!!
        holder.progress.visibility = View.VISIBLE
        Picasso.with(holder.itemView.context).load(urlPhoto).fit().memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE).into(holder.ivMainLocation,
                object : com.squareup.picasso.Callback {
                    override fun onSuccess() {
                        holder.progress.visibility = View.GONE
                    }

                    override fun onError() {
                        holder.progress.visibility = View.GONE
                    }
                })
        holder.cardView.setOnClickListener { onClick(location) }
    }

    override fun getItemCount() = locations.size




    class LocationViewHolder (view:View) : RecyclerView.ViewHolder(view){
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

    fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): Filter.FilterResults {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    locationListFiltered = locations
                } else {
                    var filteredList:List<Location> =  ArrayList()
                    for (row in locations) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.name?.toLowerCase()?.contains(charString.toLowerCase())!! ){//|| row.getPhone().contains(charSequence)) {
                            //TODO: Add this Element
                            //filteredList.add(row)
                        }
                    }

                    locationListFiltered = filteredList
                }

                val filterResults = Filter.FilterResults()
                filterResults.values = locationListFiltered
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: Filter.FilterResults) {
                locationListFiltered = filterResults.values as ArrayList<Location>
                notifyDataSetChanged()
            }
        }
    }
}