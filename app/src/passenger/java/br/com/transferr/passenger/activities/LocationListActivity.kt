package br.com.transferr.passenger.activities

import android.os.Bundle
import android.support.v4.view.MenuItemCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.view.Menu
import android.widget.EditText
import android.widget.ImageView
import br.com.transferr.R
import br.com.transferr.model.responses.OnResponseInterface
import br.com.transferr.passenger.extensions.defaultRecycleView
import br.com.transferr.passenger.extensions.setupToolbar
import br.com.transferr.passenger.extensions.showLoadingDialog
import br.com.transferr.passenger.model.Location
import br.com.transferr.passenger.webservices.WSLocation
import org.jetbrains.anko.startActivity

class LocationListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_list)
        //setHasOptionsMenu(true)
        recycleView = defaultRecycleView(this!!,R.id.rcLocationList)
        requestAllLocations()
        setupToolbar(R.id.toolbarLocationList,getString(R.string.places),true)

    }

    private var recycleView : RecyclerView?=null
    private var locationList:List<Location>?=null
    private var locationAdapter: br.com.transferr.passenger.adapter.LocationAdapter?=null


    override fun onResume() {
        super.onResume()
        setupRecyclerViewAdapter()
    }

    fun setupRecyclerViewAdapter(){
        if(locationList != null){
            locationAdapter = br.com.transferr.passenger.adapter.LocationAdapter(locationList!!, { location -> onLocationClick(location) })
            recycleView?.adapter = locationAdapter
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu_seacher,menu)
        var searchItem = menu?.findItem(R.id.menuSearch)
        var searchView = MenuItemCompat.getActionView(searchItem) as SearchView
        searchView.setOnQueryTextListener(
                object : SearchView.OnQueryTextListener{
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        //btnClickOnSearch.visibility = View.VISIBLE
                        return false
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        //btnClickOnSearch.visibility = View.GONE
                        locationAdapter?.filter?.filter(newText)
                        return false
                    }

                }
        )
        val searchEditText = searchView.findViewById<EditText>(android.support.v7.appcompat.R.id.search_src_text) as EditText
        searchEditText.setTextColor(resources.getColor(android.R.color.black))
        searchEditText.setHintTextColor(resources.getColor(android.R.color.black))
        val searchClose = searchView.findViewById<ImageView>(android.support.v7.appcompat.R.id.search_close_btn)
        searchClose.setImageResource(R.drawable.arrow_left)
        return super.onCreateOptionsMenu(menu)
    }


    private fun requestAllLocations(){
        var dialog = showLoadingDialog()
        WSLocation.getAll(object : OnResponseInterface<List<Location>> {
            override fun onSuccess(body: List<Location>?) {
                dialog?.dismiss()
                locationList = body
                setupRecyclerViewAdapter()
            }
        },this,dialog)
    }

    private fun onLocationClick(location: Location){
        startActivity<MainActivity>(Location.LOCATION to location)
        finish()
    }

}
