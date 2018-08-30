package br.com.transferr.passager.adapter

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
import br.com.transferr.passager.R
import br.com.transferr.passager.model.responses.ResponseDriver
import br.com.transferr.passager.util.WhatsAppUtil
import com.squareup.picasso.Picasso
import java.net.URLEncoder


/**
 * Created by Rafael Rocha on 25/07/18.
 */
class DriversResponseAdapter(val drivers : List<ResponseDriver>,val onClick: (ResponseDriver) -> Unit)
    :RecyclerView.Adapter<DriversResponseAdapter.DriversResponseViewHolder>()
{
    var context: Context?=null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DriversResponseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_drivers_response,parent,false)
        return DriversResponseViewHolder(view)
    }

    override fun getItemCount() = this.drivers.size

    @SuppressLint("MissingPermission")
    override fun onBindViewHolder(holder: DriversResponseViewHolder, position: Int) {
        this.context = holder!!.itemView.context
        val responseDrivers = drivers[position]
        holder.tvName.text = responseDrivers.name
        holder.tvDriverDetail.text = "${responseDrivers.countryRegister}"

        if(responseDrivers.imgProfileUrl != null && responseDrivers.imgProfileUrl?.isEmpty()!!){
            responseDrivers.imgProfileUrl = null
        }
        //holder.tvDriverPhone.text = responseDrivers.phone
        //holder.tvDriverEmail.text = responseDrivers.email
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
        //holder.cardView.setOnClickListener { onClick(responseDrivers) }
        holder.btnWhatsapp.setOnClickListener {
           //callWhatsapp(responseDrivers.whatsapp)
            WhatsAppUtil.callWhatsapp(responseDrivers.whatsapp,context!!)
        }

        holder.btnCallPhone.setOnClickListener {
            context!!.startActivity(Intent(Intent.ACTION_CALL, Uri.parse("tel:${responseDrivers.phone}")))
        }
    }

    class DriversResponseViewHolder(view: View):RecyclerView.ViewHolder(view){
        var tvName    : TextView
        var tvPrice   : TextView
        var img: ImageView
        /*
        var ivStarRate1: ImageView
        var ivStarRate2: ImageView
        var ivStarRate3: ImageView
        var ivStarRate4: ImageView
        var ivStarRate5: ImageView
        */
        var tvDriverDetail : TextView
        //var tvDriverEmail : TextView
        var cardView: CardView
        //var tvDriverPhone: TextView
        var btnCallPhone : AppCompatButton
        var btnWhatsapp : AppCompatButton
        init {
            tvName          = view.findViewById(R.id.tvNameDriver)
            img             = view.findViewById(R.id.ivProfile)
            tvPrice         = view.findViewById(R.id.tvPrice)
            tvDriverDetail  = view.findViewById(R.id.tvDriverDetail)
            cardView        = view.findViewById(R.id.cvProfessional)
            //tvDriverEmail   = view.findViewById(R.id.tvDriverEmail)
            btnCallPhone    = view.findViewById(R.id.btnCallPhone)
            btnWhatsapp     = view.findViewById(R.id.btnWhatsapp)
            /*
            //If sometime it has rate
            ivStarRate1    = view.findViewById(R.id.ivStarRate1)
            ivStarRate2    = view.findViewById(R.id.ivStarRate2)
            ivStarRate3    = view.findViewById(R.id.ivStarRate3)
            ivStarRate4    = view.findViewById(R.id.ivStarRate4)
            ivStarRate5    = view.findViewById(R.id.ivStarRate5)
            */
            //tvDriverPhone  = view.findViewById(R.id.tvDriverPhone)
        }
    }
/*
    fun callWhatsapp(phone:String){
        val packageManager = context?.getPackageManager()
        val i = Intent(Intent.ACTION_VIEW)

        try {
            val url = "https://api.whatsapp.com/send?phone=" + phone + "&text=" + URLEncoder.encode("Olá", "UTF-8")
            i.`package` = "com.whatsapp"
            i.data = Uri.parse(url)
            if (i.resolveActivity(packageManager) != null) {
                context?.startActivity(i)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
    */
}