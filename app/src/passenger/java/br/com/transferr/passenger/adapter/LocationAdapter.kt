package br.com.transferr.passenger.adapter

import android.content.Context
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import br.com.transferr.R
import br.com.transferr.passenger.model.Location
import com.squareup.picasso.Picasso

/**
 * Created by Rafael Rocha on 08/08/2018.
 */
class LocationAdapter(val locations: List<Location>, val onClick:(Location)->Unit) : RecyclerView.Adapter<br.com.transferr.passenger.adapter.LocationAdapter.LocationViewHolder>(),Filterable {

    var context:Context?=null
    //NOTE:Important! Don't forget to associate the initial list passed by parameter with the filtered list.
    var locationListFiltered:List<Location>?=locations
    //var locations: List<Location>?=null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): br.com.transferr.passenger.adapter.LocationAdapter.LocationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_location_response,parent,false)
        return br.com.transferr.passenger.adapter.LocationAdapter.LocationViewHolder(view)
    }


    override fun onBindViewHolder(holder: br.com.transferr.passenger.adapter.LocationAdapter.LocationViewHolder, position: Int) {
        val location = locationListFiltered!![position]
        holder.tvName.text = location.name
        var urlPhoto: String?
        urlPhoto = if(location.photoProfile != null && !location.photoProfile!!.isEmpty()) {
            location.photoProfile
        }else{
            null
        }

        //NOTE: This create a scroll effect on TextView
        //holder.tvLocationDescription.movementMethod = ScrollingMovementMethod()
        holder.tvLocationDescription.text = location.description
        holder.progress.visibility = View.VISIBLE
        Picasso.with(holder.itemView.context).load(urlPhoto).fit()
                //.memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .into(holder.ivMainLocation,
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
    //NOTE: Notice a important thing here that we need to use the filtered lit size instead of one passed as parameter
    override fun getItemCount() = locationListFiltered?.size!!




    class LocationViewHolder (view:View) : RecyclerView.ViewHolder(view){
        var tvName          : TextView
        var cardView        : CardView
        var ivMainLocation  : ImageView
        var tvLocationDescription : TextView
        var progress        : ProgressBar
        init {
            tvName      = view.findViewById(R.id.tvLocationName)
            cardView    = view.findViewById(R.id.cvLocation)
            ivMainLocation = view.findViewById(R.id.ivMainLocation)
            tvLocationDescription = view.findViewById(R.id.tvLocationDescription)
            progress    = view.findViewById(R.id.progress)

        }
    }



    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): Filter.FilterResults {
                val charString = charSequence.toString()
                locationListFiltered = if (charString.isEmpty()) {
                    locations
                } else {
                    var filteredList:ArrayList<Location> =  ArrayList()
                    locations.filterTo(filteredList) {
                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        it.name?.toLowerCase()?.contains(charString.toLowerCase())!!
                    }
                    filteredList
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