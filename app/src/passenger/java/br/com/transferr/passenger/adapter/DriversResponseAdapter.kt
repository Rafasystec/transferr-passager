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
import android.widget.ProgressBar
import android.widget.TextView
import br.com.transferr.R
import br.com.transferr.passenger.model.enums.EnumTypeOfDriver
import br.com.transferr.passenger.model.responses.ResponseDriver
import br.com.transferr.passenger.util.WhatsAppUtil
import com.squareup.picasso.Picasso
import java.net.URLEncoder


/**
 * Created by Rafael Rocha on 25/07/18.
 */
class DriversResponseAdapter(val drivers : List<ResponseDriver>,val onClick: (ResponseDriver) -> Unit)
    :RecyclerView.Adapter<br.com.transferr.passenger.adapter.DriversResponseAdapter.DriversResponseViewHolder>()
{
    var context: Context?=null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): br.com.transferr.passenger.adapter.DriversResponseAdapter.DriversResponseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_drivers_response,parent,false)
        return br.com.transferr.passenger.adapter.DriversResponseAdapter.DriversResponseViewHolder(view)
    }

    override fun getItemCount() = this.drivers.size

    @SuppressLint("MissingPermission")
    override fun onBindViewHolder(holder: br.com.transferr.passenger.adapter.DriversResponseAdapter.DriversResponseViewHolder, position: Int) {
        this.context = holder!!.itemView.context
        val responseDrivers         = drivers[position]
        holder.tvName.text          = responseDrivers.name
        if(responseDrivers.type == EnumTypeOfDriver.DRIVER) {
            //holder.tvDriverDetail.visibility    = View.VISIBLE
            holder.ivCarPlateIcon.visibility    = View.VISIBLE
            holder.tvCarPlate.visibility        = View.VISIBLE
            holder.ivDriverNameIcon.setImageDrawable(context!!.resources.getDrawable(R.drawable.icons8_driver_30))
            holder.tvCarPlate.text              = "${responseDrivers.countryRegister}"
        }else{
            //holder.tvDriverDetail.visibility    = View.GONE
            holder.tvCarPlate.visibility        = View.GONE
            holder.ivCarPlateIcon.visibility    = View.GONE
            holder.ivDriverNameIcon.setImageDrawable(context!!.resources.getDrawable(R.drawable.shopping_house_30))
        }
        if(responseDrivers.imgProfileUrl != null && responseDrivers.imgProfileUrl?.isEmpty()!!){
            responseDrivers.imgProfileUrl = null
        }
        holder.tvDriverDetail.text          = "${responseDrivers.nameOfCar}"
        holder.progress.visibility = View.VISIBLE
        Picasso.with(context).load(responseDrivers.imgProfileUrl).placeholder(R.drawable.no_photo_64).into(holder.img,
            object : com.squareup.picasso.Callback{
                override fun onSuccess() {
                    holder.img.visibility = View.VISIBLE
                    holder.progress.visibility = View.GONE
                }

                override fun onError() {
                    holder.img.visibility = View.VISIBLE
                    holder.progress.visibility = View.GONE
                }

            })
        holder.btnWhatsapp.setOnClickListener {
            WhatsAppUtil.callWhatsapp(responseDrivers.whatsapp,context!!)
        }

        holder.btnCallPhone.setOnClickListener {
            context!!.startActivity(Intent(Intent.ACTION_CALL, Uri.parse("tel:${responseDrivers.phone}")))
        }
    }

    class DriversResponseViewHolder(view: View):RecyclerView.ViewHolder(view){
        var tvName    : TextView = view.findViewById(R.id.tvNameDriver)
        var img: ImageView
        var ivCarPlateIcon: ImageView
        var ivDriverNameIcon:ImageView = view.findViewById(R.id.ivDriverNameIcon)
        var tvDriverDetail : TextView
        var cardView: CardView
        var btnCallPhone : AppCompatButton
        var btnWhatsapp : AppCompatButton
        var tvCarPlate:TextView = view.findViewById(R.id.tvCarPlate)
        var progress : ProgressBar = view.findViewById(R.id.progressDriverPhotoProfile)
        init {
            img             = view.findViewById(R.id.ivProfile)
            tvDriverDetail  = view.findViewById(R.id.tvDriverDetail)
            cardView        = view.findViewById(R.id.cvProfessional)
            btnCallPhone    = view.findViewById(R.id.btnCallPhone)
            btnWhatsapp     = view.findViewById(R.id.btnWhatsapp)
            ivCarPlateIcon  = view.findViewById(R.id.ivCarPlateIcon)

        }
    }

}