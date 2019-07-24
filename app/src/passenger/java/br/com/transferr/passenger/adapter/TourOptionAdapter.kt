package br.com.transferr.passenger.adapter

import android.content.Context
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import br.com.transferr.R
import br.com.transferr.application.ApplicationTransferr
import br.com.transferr.main.util.LanguageDeviceUtil
import br.com.transferr.main.util.PicassoUtil
import br.com.transferr.passenger.model.TourOption
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso

/**
 * Created by Rafael Rocha on 25/07/18.
 */
class TourOptionAdapter(val options : List<TourOption>, val onClick: (TourOption) -> Unit)
    : RecyclerView.Adapter<TourOptionAdapter.TourOptionViewHolder>()
{
    var context: Context?=null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TourOptionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_tour_option,parent,false)
        return TourOptionViewHolder(view)
    }

    override fun getItemCount() = this.options.size

    override fun onBindViewHolder(holder: TourOptionViewHolder, position: Int) {
        this.context = holder!!.itemView.context
        val tour = options[position]
        holder.tvName.text          = tour.name
        var urlPhoto: String?
        urlPhoto                    = tour.profileUrl
        holder.ivMainLocation.visibility    = View.GONE
        holder.progress.visibility          = View.VISIBLE
        Picasso.with(context)
                .load(urlPhoto!!)
                .networkPolicy(NetworkPolicy.NO_CACHE, NetworkPolicy.NO_STORE)
                .memoryPolicy(MemoryPolicy.NO_STORE)
                .error(R.drawable.no_image)
                //.placeholder(R.drawable.picasso_load_animation)
                .into(holder.ivMainLocation,object : com.squareup.picasso.Callback {
                    override fun onSuccess() {
                        holder.progress.visibility = View.GONE
                        holder.ivMainLocation.visibility = View.VISIBLE
                    }

                    override fun onError() {
                        holder.progress.visibility = View.GONE
                    }
                })
//        PicassoUtil.build(urlPhoto!!,holder.ivMainLocation,progressBar = holder.progress , callback =  object : com.squareup.picasso.Callback {
//            override fun onSuccess() {
//                holder.progress.visibility = View.GONE
//                holder.ivMainLocation.visibility = View.VISIBLE
//            }
//
//            override fun onError() {
//                holder.progress.visibility = View.GONE
//            }
//        },context = context)
        holder.cardView.setOnClickListener { onClick(tour) }
        holder.tvLocation.text = "${tour.location?.name} - ${tour.location?.subCountry}"
        var description = LanguageDeviceUtil.transform(tour.shortDescriptionLanguage!!)
        holder.tvTourDescription.text = description
    }

    class TourOptionViewHolder(view: View): RecyclerView.ViewHolder(view){
        var tvName              : TextView      = view.findViewById(R.id.tvLocationName)
        var cardView            : CardView      = view.findViewById(R.id.cvLocation)
        var ivMainLocation      : ImageView     = view.findViewById(R.id.ivMainLocation)
        var progress            : ProgressBar   = view.findViewById(R.id. progress)
        var tvTourDescription   : TextView      = view.findViewById(R.id.tvTourDescription)
        var tvLocation          : TextView      = view.findViewById(R.id.tvLocation)

    }
}