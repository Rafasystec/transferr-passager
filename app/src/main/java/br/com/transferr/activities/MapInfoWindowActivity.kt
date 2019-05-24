package br.com.transferr.passenger.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import br.com.transferr.R
import br.com.transferr.activities.SuperClassActivity
import br.com.transferr.main.util.LanguageDeviceUtil
import br.com.transferr.model.enums.EnumLanguage
//import br.com.transferr.extensions.log
import br.com.transferr.passenger.model.responses.ResponseCarsOnline
import br.com.transferr.passenger.util.DateUtil
import br.com.transferr.passenger.util.WhatsAppUtil
import com.squareup.picasso.Callback
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.inforwindows_map.*
import kotlinx.android.synthetic.main.infowindow_map_content.*


class MapInfoWindowActivity : SuperClassActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.inforwindows_map)
        var car:ResponseCarsOnline = intent.getSerializableExtra(ResponseCarsOnline.PARAM_CAR_OBJECT) as ResponseCarsOnline
        buildMapInfoWindow(car)
        btnCloseInfoWindow.setOnClickListener {
            finish()
        }
        relativeMapInfoWindow.setOnClickListener {
            finish()
        }
    }

    @SuppressLint("MissingPermission")
    private fun buildMapInfoWindow(car: ResponseCarsOnline) {
        if(car == null) return
        name?.text = car.name
        var model: String
        var plate: String
        var color: String
        var date : String
        var url  : String? = null
        if (car != null) {
            model = "${this?.getString(R.string.car)}: ${car.model!!}"
            plate = "${this?.getString(R.string.plate)}: ${car.placa!!}"
            color = "${this?.getString(R.string.color)}: ${LanguageDeviceUtil.getColorAsString(car.cor!!) }"
            url   = car.photo!!
            date  = "${DateUtil.getDateByLanguageAsString(car.date!!)}"
        } else {
            model = "${this.getString(R.string.car)}: ${this.getString(R.string.noInfor)}"
            plate = "${this.getString(R.string.plate)}: ${this.getString(R.string.noInfor)}"
            color = "${this.getString(R.string.color)}: ${this.getString(R.string.noInfor)}"
            date  = ""
        }
        tvMapCarModel?.text    = model
        tvMapPlate?.text       = plate
        tvMapCarColor?.text    = color
        tvLastUpdate.text      = date
        setImageFromPicasso(url)
        btnWhatsapp?.setOnClickListener {
            WhatsAppUtil.callWhatsapp(""+car.whatsapp,context)
        }

        btnCallPhone?.setOnClickListener {
            this.startActivity(Intent(Intent.ACTION_CALL, Uri.parse("tel:${car.phone}")))
        }
    }

    private fun setImageFromPicasso(url: String?) {
        progressDriverPhoto.visibility = View.VISIBLE
        Picasso.with(this)
                .load(url)
                .fit()
                .centerInside()
                .priority(Picasso.Priority.HIGH)
                .placeholder(R.drawable.loadrealimg)
                .memoryPolicy(MemoryPolicy.NO_STORE,MemoryPolicy.NO_CACHE)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .into(photo,
                        object : Callback {
                            override fun onSuccess() {
                                //log("On Success!")
                                progressDriverPhoto.visibility = View.INVISIBLE
                                photo.visibility = View.VISIBLE
                            }

                            override fun onError() {
                                //log("On Error photo driver")
                                progressDriverPhoto.visibility = View.GONE
                                photo.visibility = View.VISIBLE
                            }
                        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.close_cycle_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when(item?.itemId){
            R.id.menuClose ->{
               finish()
               true
            }else ->return super.onOptionsItemSelected(item)
        }

    }

}
