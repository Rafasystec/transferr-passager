package br.com.transferr.passager.activities

import android.content.Intent
import android.os.Bundle
import br.com.transferr.passager.R
import br.com.transferr.passager.interfaces.OnResponseInterface
import br.com.transferr.passager.model.Location
import br.com.transferr.passager.model.TourOption
import br.com.transferr.passager.model.responses.ResponseLocation
import br.com.transferr.passager.webservices.WSLocation
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.layout_location_details.*
import org.jetbrains.anko.progressDialog

@Deprecated("Don't use anymore")
class LocationActivity : SuperClassActivity() {

    var idLocation:Long?=null
    var tourOption:TourOption?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)
        tourOption = intent.getSerializableExtra(TourOption.TOUR_PARAMETER_KEY) as TourOption
        btnSeeDrivers.setOnClickListener {
            var intent = Intent(this,DriverListActivity::class.java)
            intent.putExtra(ResponseLocation.LOCATION_PARAMETER_KEY,idLocation!!)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        //loadLocation()
        loadFields()
    }
/*
    private fun loadLocation() {

        /*
        val dialog = progressDialog(message = "Carregando os campos",title = "Aguarde...")
        WSLocation.doGetById(idLocation!!, object :OnResponseInterface<Location>{
            override fun onSuccess(body: Location?) {
                loadFields(body!!)
                dialog.dismiss()
            }

            override fun onError(message: String) {
                alertWarning(message)
                dialog.dismiss()
            }

            override fun onFailure(t: Throwable?) {
                alertErro(t?.message!!)
                dialog.dismiss()
            }

        })
        */
    }
*/
    private fun loadFields(){
        if(tourOption == null){
            return
        }
        Picasso.with(this).load(tourOption?.profileUrl).memoryPolicy(MemoryPolicy.NO_STORE,MemoryPolicy.NO_CACHE).into(ivMainPicture)
        tvLocationTitle.text = tourOption?.name
        tvLocationDescription.text = tourOption?.description
        var i = 0
        tourOption?.images?.forEach {
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
