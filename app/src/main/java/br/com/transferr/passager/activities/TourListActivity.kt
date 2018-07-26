package br.com.transferr.passager.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import br.com.transferr.passager.R
import br.com.transferr.passager.adapter.LocationResponseAdapter
import br.com.transferr.passager.extensions.defaultRecycleView
import br.com.transferr.passager.model.responses.ResponseLocation

class TourListActivity : SuperClassActivity() {

    private var recycleView : RecyclerView?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tour_list)
        recycleView = defaultRecycleView(this,R.id.rcTourList)
    }

    override fun onResume() {
        super.onResume()
        loadTourList()
    }

    private fun loadTourList() {
        var location = ResponseLocation()
        with(location){
            name = "Lagoinha"
        }
        var location2 = ResponseLocation()
        with(location2){
            name = "Flecheira"
        }
        recycleView!!.adapter = LocationResponseAdapter(listOf(location,location2),
                onClick = {responseLocation: ResponseLocation -> onLocationClick(responseLocation) })
    }
    fun onLocationClick(responseLocation: ResponseLocation){
        startActivity(Intent(this,LocationActivity::class.java))
    }
}
