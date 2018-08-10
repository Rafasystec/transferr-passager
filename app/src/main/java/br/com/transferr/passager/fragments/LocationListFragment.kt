package br.com.transferr.passager.fragments


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.MenuItemCompat
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.view.*
import br.com.transferr.fragments.SuperClassFragment

import br.com.transferr.passager.R
import br.com.transferr.passager.activities.LocationDetailActivity
import br.com.transferr.passager.adapter.LocationAdapter
import br.com.transferr.passager.extensions.defaultRecycleView
import br.com.transferr.passager.extensions.setupToolbar
import br.com.transferr.passager.extensions.switchFragmentToMainContent
import br.com.transferr.passager.interfaces.OnResponseInterface
import br.com.transferr.passager.model.Location
import br.com.transferr.passager.webservices.WSLocation
import kotlinx.android.synthetic.main.fragment_location_list.*
import org.jetbrains.anko.progressDialog
import org.jetbrains.anko.toast


/**
 * A simple [Fragment] subclass.
 * @author Rafael Rocha on 08/08/2018
 */
class LocationListFragment : SuperClassFragment() {

    private var recycleView : RecyclerView?=null
    private var locationList:List<Location>?=null
    private var locationAdapter:LocationAdapter?=null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {


        return inflater.inflate(R.layout.fragment_location_list, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        setupToolbar(R.id.toolbar,"Locais")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycleView = defaultRecycleView(activity!!,R.id.rcLocationList)
        initSearchView()
        requestAllLocations()
    }

    override fun onResume() {
        super.onResume()
        setupRecyclerViewAdapter()
    }

    fun setupRecyclerViewAdapter(){
        if(locationList != null){
            locationAdapter = LocationAdapter(locationList!!,{location -> onLocationClick(location) })
            recycleView?.adapter = locationAdapter
        }
    }

    fun requestAllLocations(){
        var dialog = activity?.progressDialog(message = R.string.loading, title = R.string.wait)
        WSLocation.getAll(object : OnResponseInterface<List<Location>>{
            override fun onSuccess(body: List<Location>?) {
                dialog?.dismiss()
                locationList = body
                setupRecyclerViewAdapter()
            }

            override fun onError(message: String) {
                dialog?.dismiss()
            }

            override fun onFailure(t: Throwable?) {
                dialog?.dismiss()
            }

        })
    }

    fun onLocationClick(location: Location){
        //var intent = Intent(context,LocationDetailActivity::class.java)
        //intent.putExtra(Location.LOCATION,location)
        //startActivity(intent)
        var fragment = TourOptionLisFragment()
        fragment.arguments = Bundle()
        fragment.arguments!!.putSerializable(Location.LOCATION,location)
        switchFragmentToMainContent(fragment)
    }

    fun initSearchView(){
        searchView.setOnQueryTextListener(
                object : SearchView.OnQueryTextListener{
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        return false
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        locationAdapter?.filter?.filter(newText)
                        return false
                    }

                }
        )
    }

}
