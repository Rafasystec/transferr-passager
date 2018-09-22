package br.com.transferr.passenger.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.support.v4.app.Fragment
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import br.com.transferr.R
import br.com.transferr.passenger.extensions.fromJson
import br.com.transferr.passenger.fragments.MapsFragment
import br.com.transferr.passenger.model.responses.ResponseCarsOnline
import br.com.transferr.passenger.util.WhatsAppUtil
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.Marker
import com.squareup.picasso.Callback
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.passenger.inforwindows_map.view.*


/**
 * Created by Rafael Rocha on 23/02/2018.
 */
class MapInfoWindowsAdapter(fragment: MapsFragment) : GoogleMap.InfoWindowAdapter,GoogleMap.OnMarkerClickListener {

    override fun onMarkerClick(p0: Marker?): Boolean {
        //Toast.makeText(context,"Clicou no marque",Toast.LENGTH_SHORT)

        return false
    }
    var context:MapsFragment = fragment
    var latestMarkerId:String = "0"
    companion object {
        var isFirstTime = true
    }

    override fun getInfoContents(marker: Marker?): View?  = null


    private fun buildMapInfoWindow(markerParam: Marker?): View? {
       // context.marker = marker
        var isShown = markerParam?.isInfoWindowShown
        var active = this.context.activity
        var mapView = active?.layoutInflater?.inflate(R.layout.inforwindows_map, null)
        mapView?.name?.text = markerParam?.title
        //mapView.details.text    = marker?.snippet
        var jsonCar = markerParam?.snippet
        var car = fromJson<ResponseCarsOnline>(jsonCar!!)
        var model: String
        var plate: String
        var color: String
        var url: String? = null
        if (car != null) {
            model = "${active?.getString(R.string.model)}: ${car.model!!}"
            plate = "${active?.getString(R.string.plate)}: ${car.placa!!}"
            color = "${active?.getString(R.string.color)}: ${car.cor!!}"
            url = car.photo!!
        } else {
            model = "${active?.getString(R.string.model)}: ${active?.getString(R.string.noInfor)}"
            plate = "${active?.getString(R.string.plate)}: ${active?.getString(R.string.noInfor)}"
            color = "${active?.getString(R.string.color)}: ${active?.getString(R.string.noInfor)}"
        }
        mapView?.tvMapCarModel?.text    = model
        mapView?.tvMapPlate?.text       = plate
        mapView?.tvMapCarColor?.text    = color
        //mapView?.progressImg?.visibility = View.VISIBLE
        if (isFirstTime) {
            setImageFromPicasso(url, mapView, markerParam)
        }else{
            isFirstTime = true

            Picasso.with(context.context)
                    .load(url)
                    .priority(Picasso.Priority.HIGH)
                    .into(mapView?.photo)
        }
        mapView?.btnWhatsapp?.setOnClickListener {
            WhatsAppUtil.callWhatsapp(""+car.whatsapp,context.context!!)
        }

        mapView?.btnCallPhone?.setOnClickListener {
            context!!.startActivity(Intent(Intent.ACTION_CALL, Uri.parse("tel:${car.phone}")))
        }

        return mapView
    }

    private fun setImageFromPicasso(url: String?, mapView: View?, markerParam: Marker?) {
        Picasso.with(context.context)
                .load(url)
                .priority(Picasso.Priority.HIGH)
                .placeholder(R.drawable.loadrealimg)
                .into(mapView?.photo,
                        object : Callback {
                            override fun onSuccess() {
                                if(isFirstTime) {
                                    isFirstTime = false
                                    markerParam?.showInfoWindow()
                                }else{
                                    isFirstTime = true
                                }
                            }

                            override fun onError() {

                            }
                        })
    }

    override fun getInfoWindow(marker: Marker?): View? {
        return buildMapInfoWindow(marker)
    }
}