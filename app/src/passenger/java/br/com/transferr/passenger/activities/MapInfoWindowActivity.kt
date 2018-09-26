package br.com.transferr.passenger.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import br.com.transferr.R
import br.com.transferr.passenger.model.responses.ResponseCarsOnline
import br.com.transferr.passenger.util.WhatsAppUtil
import com.squareup.picasso.Callback
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.infowindow_map_content.*
import kotlinx.android.synthetic.passenger.inforwindows_map.*

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
        var url: String? = null
        if (car != null) {
            model = "${this?.getString(R.string.model)}: ${car.model!!}"
            plate = "${this?.getString(R.string.plate)}: ${car.placa!!}"
            color = "${this?.getString(R.string.color)}: ${car.cor!!}"
            url = car.photo!!
        } else {
            model = "${this.getString(R.string.model)}: ${this.getString(R.string.noInfor)}"
            plate = "${this.getString(R.string.plate)}: ${this.getString(R.string.noInfor)}"
            color = "${this.getString(R.string.color)}: ${this.getString(R.string.noInfor)}"
        }
        tvMapCarModel?.text    = model
        tvMapPlate?.text       = plate
        tvMapCarColor?.text    = color
        setImageFromPicasso(url)
        btnWhatsapp?.setOnClickListener {
            WhatsAppUtil.callWhatsapp(""+car.whatsapp,context)
        }

        btnCallPhone?.setOnClickListener {
            this.startActivity(Intent(Intent.ACTION_CALL, Uri.parse("tel:${car.phone}")))
        }
    }

    private fun setImageFromPicasso(url: String?) {
        Picasso.with(this)
                .load(url)
                .priority(Picasso.Priority.HIGH)
                .placeholder(R.drawable.loadrealimg)
                .memoryPolicy(MemoryPolicy.NO_STORE,MemoryPolicy.NO_CACHE)
                .into(photo,
                        object : Callback {
                            override fun onSuccess() {

                            }

                            override fun onError() {

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
