package br.com.transferr.passenger.adapter

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import br.com.transferr.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.passenger.inforwindows_map.view.*


/**
 * Created by Rafael Rocha on 23/02/2018.
 */
class MapInfoWindowsAdapter(context: Context) : GoogleMap.InfoWindowAdapter,GoogleMap.OnMarkerClickListener {

    override fun onMarkerClick(p0: Marker?): Boolean {
        Toast.makeText(context,"Clicou no marque",Toast.LENGTH_SHORT)

        return false
    }
    var context:Context = context

    override fun getInfoContents(marker: Marker?): View {
        var active = this.context as Activity
        var mapView = active.layoutInflater.inflate(R.layout.inforwindows_map,null)
        mapView.name.text       = marker?.title
        mapView.details.text    = marker?.snippet

        var url = "http://planetcarsz.com/assets/uploads/images/VEICULOS/T/TOYOTA/2014_TOYOTA_HILUX//TOYOTA_HILUX_2014_01.jpg"
        loadImage(marker,mapView.photo,url)
        mapView.txtDistance.text = "16km"
        return mapView
    }

    fun test(){

    }

    private fun loadImage(marker: Marker?,image:ImageView,url:String){
        Picasso.with(context).load(url)
                .priority(Picasso.Priority.HIGH)
                .placeholder(R.mipmap.toyota_sw4_08)
                .into(image,object : Callback{
                    override fun onSuccess() {
                        if(marker?.isInfoWindowShown!!){
                            marker.hideInfoWindow()
                            marker.showInfoWindow()
                        }

                    }
                    override fun onError() {

                    }
                })


    }

    override fun getInfoWindow(marker: Marker?): View? {
        return null
    }
}