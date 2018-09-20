package br.com.transferr.passenger.adapter

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import br.com.transferr.R
import br.com.transferr.passenger.extensions.fromJson
import br.com.transferr.passenger.model.responses.ResponseCarsOnline
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.squareup.picasso.Callback
import com.squareup.picasso.MemoryPolicy
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
        //mapView.details.text    = marker?.snippet
        var jsonCar = marker?.snippet
        var car = fromJson<ResponseCarsOnline>(jsonCar!!)
        var model: String
        var plate: String
        var color: String
        var url   :String?=null
        if(car != null){
            model   = "${active.getString(R.string.model)}: ${car.model!!}"
            plate   = "${active.getString(R.string.plate)}: ${car.placa!!}"
            color   = "${active.getString(R.string.color)}: ${car.cor!!}"
            url = car.photo!!
        }else{
            model   = "${active.getString(R.string.model)}: ${active.getString(R.string.noInfor)}"
            plate   = "${active.getString(R.string.plate)}: ${active.getString(R.string.noInfor)}"
            color   = "${active.getString(R.string.color)}: ${active.getString(R.string.noInfor)}"
        }
        mapView.tvMapCarModel.text  = model
        mapView.tvMapPlate.text     = plate
        mapView.tvMapCarColor.text  = color

        //var url = "http://planetcarsz.com/assets/uploads/images/VEICULOS/T/TOYOTA/2014_TOYOTA_HILUX//TOYOTA_HILUX_2014_01.jpg"
        //loadImage(marker,mapView.photo,url!!,mapView)
        mapView.progressImg.visibility = View.VISIBLE

        Picasso.with(context).load(url)
                .priority(Picasso.Priority.HIGH)
                .placeholder(R.drawable.no_photo_64)
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .into(mapView.photo,object : Callback {
                    override fun onSuccess() {
                        if(marker?.isInfoWindowShown!!){
                            marker.hideInfoWindow()
                            marker.showInfoWindow()
                        }
                        mapView.progressImg.visibility = View.GONE

                    }
                    override fun onError() {
                        mapView.progressImg.visibility = View.GONE
                    }
                })

        return mapView
    }

    fun test(){

    }

    private fun loadImage(marker: Marker?,image:ImageView,url:String,view: View){
        view.progressImg.visibility = View.VISIBLE

        Picasso.with(context).load(url)
                .priority(Picasso.Priority.HIGH)
                .placeholder(R.drawable.no_photo_64)
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .into(image,object : Callback {
                    override fun onSuccess() {
                        if(marker?.isInfoWindowShown!!){
                            marker.hideInfoWindow()
                            marker.showInfoWindow()
                        }
                        view.progressImg.visibility = View.GONE

                    }
                    override fun onError() {
                        view.progressImg.visibility = View.GONE
                    }
                })


    }

    override fun getInfoWindow(marker: Marker?): View? {
        return null
    }
}