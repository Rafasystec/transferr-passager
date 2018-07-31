package br.com.transferr.passager.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import br.com.transferr.passager.R
import br.com.transferr.passager.interfaces.OnResponseInterface
import br.com.transferr.passager.model.Location
import br.com.transferr.passager.model.responses.ResponseLocation
import br.com.transferr.passager.webservices.WSLocation
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.layout_location_details.*
import org.jetbrains.anko.toast

class LocationActivity : SuperClassActivity() {

    var idLocation:Long?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)
        idLocation = intent.getLongExtra(ResponseLocation.LOCATION_PARAMETER_KEY,0L)
        btnSeeDrivers.setOnClickListener {
            var intent = Intent(this,DriverListActivity::class.java)
            intent.putExtra(ResponseLocation.LOCATION_PARAMETER_KEY,idLocation!!)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        loadLocation()
    }

    private fun loadLocation() {
        WSLocation.doGetById(idLocation!!, object :OnResponseInterface<Location>{
            override fun onSuccess(body: Location?) {
                loadFields(body!!)
            }

            override fun onError(message: String) {
                alertWarning(message)
            }

            override fun onFailure(t: Throwable?) {
                alertErro(t?.message!!)
            }

        })
    }

    fun loadFields(location: Location){
        Picasso.with(this).load(location.photoProfile).into(ivMainPicture)
        tvLocationTitle.text = location.name
        tvLocationDescription.text = location.description
        var i = 0
        location.images?.forEach {
            ++i
            when (i) {
                1 -> Picasso.with(this).load(it).into(ivPictureOne)
                2 -> Picasso.with(this).load(it).into(ivPictureTwo)
                3 -> Picasso.with(this).load(it).into(ivPictureThree)
                4 -> Picasso.with(this).load(it).into(ivPictureFour)
                5 -> Picasso.with(this).load(it).into(ivPictureFive)
                6 -> Picasso.with(this).load(it).into(ivPictureSix)
            }
        }
    }
}
